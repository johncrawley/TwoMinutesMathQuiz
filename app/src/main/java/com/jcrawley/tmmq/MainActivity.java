package com.jcrawley.tmmq;

import static com.jcrawley.tmmq.view.fragments.GameOverFragment.*;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.*;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Tag.*;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;
import com.jcrawley.tmmq.view.fragments.OptionsFragment;

import java.util.Optional;


public class MainActivity extends AppCompatActivity {

    private GameService gameService;
    private MainViewModel viewModel;
    private Vibrator vibrator;
    private boolean isVibrationEnabled;


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            sendMessage(OptionsFragment.Message.NOTIFY_OF_SERVICE_CONNECTED);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            log("Entered onServiceDisconnected()");
        }
    };


    private void log(String msg) {
        System.out.println("^^^ MainActivity: " + msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupVibe();
        if (savedInstanceState == null) {
            setupFragments();
        }
        setupGameService();
    }


    public void assignVibrationSettings() {
        isVibrationEnabled = getPrefs().getBoolean("vibration_enabled", true);
    }


    public Optional<GameService> getGameService(){
        return Optional.ofNullable(gameService);
    }


    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    private void setupFragments() {
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }


    public void playSound(Sound sound) {
        reassignActivityToService();
        gameService.playSound(sound);
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


    public void fadeInQuestionText(String questionText) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION.toString(), questionText);
        sendMessage(SET_QUESTION, bundle);
    }


    public void setQuestion(MathQuestion question) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION.toString(), question.getQuestionText());
        bundle.putBoolean(IS_QUESTION_USING_AN_EXPONENT.toString(), question.containsExponent());
        sendMessage(SET_QUESTION, bundle);
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void setTimeRemaining(int minutesRemaining, int secondsRemaining) {
        Bundle bundle = new Bundle();
        bundle.putInt(MINUTES_REMAINING.toString(), minutesRemaining);
        bundle.putInt(SECONDS_REMAINING.toString(), secondsRemaining);
        sendMessage(SET_TIME_REMAINING, bundle);
    }


    public void onGameOver(ScoreStatistics scoreStatistics) {
        Bundle bundle = new Bundle();
        bundle.putInt(FINAL_SCORE_KEY, scoreStatistics.getFinalScore());
        bundle.putInt(DAILY_HIGH_SCORE_KEY, scoreStatistics.getDailyHighScore());
        bundle.putInt(ALL_TIME_HIGH_SCORE_KEY, scoreStatistics.getAllTimeHighScore());
        bundle.putString(TIMER_LENGTH_KEY, scoreStatistics.getTimerLength());
        bundle.putString(GAME_LEVEL_KEY, scoreStatistics.getGameLevel().getDifficultyStr());
        sendMessage(NOTIFY_GAME_OVER, bundle);
    }


    public void notifyServiceThatGameHasFinished() {
        reassignActivityToService();
        gameService.notifyThatGameFinished();
    }


    public void setScore(int score) {
        Bundle bundle = new Bundle();
        bundle.putInt(SCORE.toString(), score);
        sendMessage(SET_SCORE, bundle);
    }


    public void notifyIncorrectAnswer() {
        sendMessage(NOTIFY_INCORRECT_ANSWER, new Bundle());
    }


    public void submitAnswer(String answerStr) {
        reassignActivityToService();
        gameService.submitAnswer(answerStr);
    }


    public void startGame() {
        assignVibrationSettings();
        if (gameService == null) {
            return;
        }
        reassignActivityToService();
        gameService.startGame();
    }


    public void stopGame() {
        if (gameService == null) {
            return;
        }
        reassignActivityToService();
        gameService.quitGame();
    }


    private void reassignActivityToService() {
        if (gameService == null) {
            return;
        }
        if (gameService.isActivityUnbound()) {
            gameService.setActivity(MainActivity.this);
        }
    }


    public <E extends Enum<E>> void sendMessage(E operationName, Bundle bundle) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), bundle);
    }


    public <E extends Enum<E>> void sendMessage(E operationName) {
        getSupportFragmentManager().setFragmentResult(operationName.toString(), new Bundle());
    }


}