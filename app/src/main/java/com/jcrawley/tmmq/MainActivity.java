package com.jcrawley.tmmq;

import static com.jcrawley.tmmq.view.fragments.message.BundleKey.IS_QUESTION_USING_AN_EXPONENT;
import static com.jcrawley.tmmq.view.fragments.message.BundleKey.MINUTES_REMAINING;
import static com.jcrawley.tmmq.view.fragments.message.BundleKey.QUESTION;
import static com.jcrawley.tmmq.view.fragments.message.BundleKey.SCORE;
import static com.jcrawley.tmmq.view.fragments.message.BundleKey.SECONDS_REMAINING;
import static com.jcrawley.tmmq.view.fragments.message.MessageKey.NOTIFY_INCORRECT_ANSWER;
import static com.jcrawley.tmmq.view.fragments.message.MessageKey.SET_QUESTION;
import static com.jcrawley.tmmq.view.fragments.message.MessageKey.SET_SCORE;
import static com.jcrawley.tmmq.view.fragments.message.MessageKey.SET_TIME_REMAINING;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentLoader.loadGameOverFragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.GameView;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.game.timer.GameTimer;
import com.jcrawley.tmmq.service.preferences.GamePreferenceManagerImpl;
import com.jcrawley.tmmq.service.score.date.CurrentDateGeneratorImpl;
import com.jcrawley.tmmq.service.score.preferences.ScorePreferencesImpl;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.saver.ScoreSaverImpl;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.service.sound.SoundPlayer;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;


public class MainActivity extends AppCompatActivity implements GameView {

    private MainViewModel viewModel;
    private Vibrator vibrator;
    private boolean isVibrationEnabled;
    private Game game;
    private SoundPlayer soundPlayer;


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
        setupGame();
    }


    private void setupGame(){
        var gamePreferenceManager = new GamePreferenceManagerImpl(this);
        var scorePreferences = new ScorePreferencesImpl(getScorePrefs());
        var currentDateGenerator = new CurrentDateGeneratorImpl();
        var scoreRecords = new ScoreRecords(scorePreferences, currentDateGenerator);
        var scoreSaver = new ScoreSaverImpl(scorePreferences, currentDateGenerator);
        game = new Game(MainActivity.this, gamePreferenceManager, scoreRecords, scoreSaver);
        var gameTimer = new GameTimer(game, viewModel.gameTimerModel);
        game.init(viewModel.gameModel, gameTimer);
    }


    @Override
    public void loadGameOverScreen(){
    //    new Handler(Looper.getMainLooper()).postDelayed(()-> loadGameOverFragment(), 2000);
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


    public Game getGame(){
        return game;
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


    public void playSound(Sound sound) {
        soundPlayer.playSound(sound);
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
    public void updateTimer(int minutesRemaining, int secondsRemaining) {
        var bundle = new Bundle();
        addTo(bundle, MINUTES_REMAINING, minutesRemaining);
        addTo(bundle, MINUTES_REMAINING, minutesRemaining);
        addTo(bundle, SECONDS_REMAINING, secondsRemaining);
        sendMessage(SET_TIME_REMAINING, bundle);
    }


    @Override
    public void setQuestion(MathQuestion question) {
        Bundle bundle = new Bundle();
        addTo(bundle, QUESTION, question.getQuestionText());
        addTo(bundle, IS_QUESTION_USING_AN_EXPONENT, question.containsExponent());
        sendMessage(SET_QUESTION, bundle);
    }


    public void setScore(int score) {
        var bundle = new Bundle();
        addTo(bundle, SCORE, score);
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


    public SharedPreferences getScorePrefs(){
        return getSharedPreferences("score_preferences", MODE_PRIVATE);
    }


    public <E extends Enum<E>> void addTo(Bundle bundle, E key, int value){
        bundle.putInt(key.toString(), value);
    }


    public <E extends Enum<E>> void addTo(Bundle bundle, E key, String value){
        bundle.putString(key.toString(), value);
    }


    public <E extends Enum<E>> void addTo(Bundle bundle, E key, boolean value){
        bundle.putBoolean(key.toString(), value);
    }


    public <E extends Enum<E>> void sendMessage(E operationName, Bundle bundle) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), bundle);
    }


    public <E extends Enum<E>> void sendMessage(E operationName) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), new Bundle());
    }


}