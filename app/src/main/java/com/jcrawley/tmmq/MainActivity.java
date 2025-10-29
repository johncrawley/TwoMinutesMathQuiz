package com.jcrawley.tmmq;

import static com.jcrawley.tmmq.view.fragments.GameOverFragment.*;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.*;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Tag.*;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.GameView;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.preferences.GamePreferenceManager;
import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.service.sound.SoundPlayer;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;
import com.jcrawley.tmmq.view.fragments.OptionsFragment;

import java.util.Optional;


/*


    void resetScore();
    void setQuestion(MathQuestion question);
    void notifyIncorrectAnswer();
    void setScore(int score);
    void gameOver(ScoreStatistics scoreStatistics);
    void updateTimer(int minutesRemaining, int secondsRemaining);
 */

public class MainActivity extends AppCompatActivity implements GameView {

    private MainViewModel viewModel;
    private Vibrator vibrator;
    private boolean isVibrationEnabled;
    private Game game;
    private SoundPlayer soundPlayer;
    private GamePreferenceManager gamePreferenceManager;


    public void onServiceConnected(ComponentName className, IBinder service) {
        sendMessage(OptionsFragment.Message.NOTIFY_OF_SERVICE_CONNECTED);
    }


    private void log(String msg) {
        System.out.println("^^^ MainActivity: " + msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupInsetPadding();
        configureNavAndStatusBarAppearance();
        setupViewModel();
        setupVibe();
        setupFragmentsIf(savedInstanceState == null);

        soundPlayer = new SoundPlayer(getApplicationContext());
        gamePreferenceManager = new GamePreferenceManager(this);
        game = new Game(MainActivity.this);
    }


    private void setupInsetPadding(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    private void configureNavAndStatusBarAppearance(){
        var window = getWindow();
        var insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.setAppearanceLightNavigationBars(false);
        insetsController.setAppearanceLightStatusBars(false);
    }


    @Override
    protected void onResume(){
        super.onResume();
    }


    public void assignVibrationSettings() {
        isVibrationEnabled = getPrefs().getBoolean("vibration_enabled", true);
    }


    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    private void setupFragmentsIf(boolean isSavedInstanceStateNull) {
        if (!isSavedInstanceStateNull) {
            return;
        }
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }


    public void playSound(Sound sound) {
    }


    public void playSound(Sound sound, int delay) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> playSound(sound), delay);
    }


    private void setupVibe() {
        assignVibrationSettings();
        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
    }


    public void onKeypadButtonClicked() {
        vibrate();
        playSound(Sound.KEYPAD_BUTTON);
    }


    private void vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (isVibrationEnabled) {
                vibrator.vibrate(VibrationEffect.createOneShot(55, 1));
            }
        }
    }


    public MainViewModel getViewModel() {
        return viewModel;
    }


    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


    @Override
    public void resetScore() {

    }


    @Override
    public void gameOver(ScoreStatistics scoreStatistics) {

    }


    @Override
    public void updateTimer(int minutesRemaining, int secondsRemaining) {

    }

    @Override
    public void setQuestion(MathQuestion question) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION.toString(), question.getQuestionText());
        bundle.putBoolean(IS_QUESTION_USING_AN_EXPONENT.toString(), question.containsExponent());
        sendMessage(SET_QUESTION, bundle);
    }


    public void setTimeRemaining(int minutesRemaining, int secondsRemaining) {
        Bundle bundle = new Bundle();
        bundle.putInt(MINUTES_REMAINING.toString(), minutesRemaining);
        bundle.putInt(SECONDS_REMAINING.toString(), secondsRemaining);
        sendMessage(SET_TIME_REMAINING, bundle);
    }


    public void onGameOver(ScoreStatistics scoreStatistics) {
        Bundle bundle = new Bundle();
        addTo(bundle, Key.FINAL_SCORE, scoreStatistics.getFinalScore());
        addTo(bundle, Key.DAILY_HIGH_SCORE, scoreStatistics.getExistingDailyHighScore());
        addTo(bundle, Key.HIGH_SCORE, scoreStatistics.getExistingHighScore());
        addTo(bundle, Key.TIMER_LENGTH, scoreStatistics.getTimerLength());
        addTo(bundle, Key.GAME_LEVEL, scoreStatistics.getGameLevel().getDifficultyStr());

        sendMessage(NOTIFY_GAME_OVER, bundle);
    }


    public void notifyServiceThatGameHasFinished() {
        game.notifyThatGameFinished();
    }


    public void setScore(int score) {
        Bundle bundle = new Bundle();
        bundle.putInt(SCORE.toString(), score);
        sendMessage(SET_SCORE, bundle);
    }

    @Override
    public void notifyIncorrectAnswer() {
        sendMessage(NOTIFY_INCORRECT_ANSWER, new Bundle());
    }


    public void submitAnswer(String answerStr) {
        game.submitAnswer(answerStr);
    }


    public void startGame() {
        assignVibrationSettings();
        game.startGame();
    }


    public void stopGame() {
        game.quit();
    }



    public <E extends Enum<E>> void addTo(Bundle bundle, E key, int value){
        bundle.putInt(key.toString(), value);
    }


    public <E extends Enum<E>> void addTo(Bundle bundle, E key, String value){
        bundle.putString(key.toString(), value);
    }


    public <E extends Enum<E>> void sendMessage(E operationName, Bundle bundle) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), bundle);
    }


    public <E extends Enum<E>> void sendMessage(E operationName) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), new Bundle());
    }


}