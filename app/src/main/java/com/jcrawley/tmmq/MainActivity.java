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
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.TimerLength;
import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;


public class MainActivity extends AppCompatActivity {

    private GameService gameService;
    private MainViewModel viewModel;
    private Vibrator vibrator;
    private boolean isVibrationEnabled;
    private FragmentContainerView fragmentContainerView;


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            log("Entered onServiceDisconnected()");
        }
    };


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupVibe();
        if(savedInstanceState == null){
            setupFragments();
        }
        setupGameService();
     }


    public void assignVibrationSettings() {
        isVibrationEnabled = getPrefs().getBoolean("vibration_enabled", true);
    }


    private SharedPreferences getPrefs(){
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    private void setupFragments(){
        fragmentContainerView = findViewById(R.id.fragment_container);

        Fragment welcomeScreenFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, welcomeScreenFragment)
        .commit();
     }


     private void setupVibe(){
        assignVibrationSettings();
        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
     }


     public void vibrateOnPress(){
         if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             if(isVibrationEnabled) {
                 vibrator.vibrate(VibrationEffect.createOneShot(55, 1));
             }
         }
     }


     public MainViewModel getViewModel(){
        return viewModel;
     }


     private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
     }


    public void fadeInQuestionText(String questionText){
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION.toString(), questionText);
        sendMessage(SET_QUESTION, bundle);
    }


    public void setQuestionText(String questionText){
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION.toString(), questionText);
        sendMessage(SET_QUESTION, bundle );
    }


    private void setupGameService(){
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void setTimeRemaining(int minutesRemaining, int secondsRemaining){
        Bundle bundle = new Bundle();
        bundle.putInt(MINUTES_REMAINING.toString(), minutesRemaining);
        bundle.putInt(SECONDS_REMAINING.toString(), secondsRemaining);
        sendMessage(SET_TIME_REMAINING, bundle);
    }


    public void onGameOver(ScoreStatistics scoreStatistics){
        Bundle bundle = new Bundle();
        bundle.putInt(FINAL_SCORE_KEY, scoreStatistics.getFinalScore());
        bundle.putInt(DAILY_HIGH_SCORE_KEY, scoreStatistics.getDailyHighScore());
        bundle.putInt(ALL_TIME_HIGH_SCORE_KEY, scoreStatistics.getAllTimeHighScore());
        bundle.putString(TIMER_LENGTH_KEY, scoreStatistics.getTimerLength());
        bundle.putString(GAME_LEVEL_KEY, scoreStatistics.getGameLevel().getDifficultyStr());
        sendMessage(NOTIFY_GAME_OVER, bundle);
    }


    public void notifyServiceThatGameHasFinished(){
        reassignActivityToService();
        gameService.notifyThatGameFinished();
    }


    public void setScore(int score){
         Bundle bundle = new Bundle();
         bundle.putInt(SCORE.toString(), score);
         sendMessage(SET_SCORE, bundle);
    }


    public void notifyIncorrectAnswer(){
        sendMessage(NOTIFY_INCORRECT_ANSWER, new Bundle());
    }


    public void submitAnswer(String answerStr){
        reassignActivityToService();
        gameService.submitAnswer(answerStr);
    }


    public void startGame(){
        assignVibrationSettings();
        if(gameService == null){
            return;
        }
        reassignActivityToService();
        gameService.startGame(getTimerLength());
    }


    public void stopGame(){
        if(gameService == null){
            return;
        }
        reassignActivityToService();
        gameService.quitGame();
    }


    public TimerLength getTimerLength(){
        int selectedTime = Integer.parseInt(getPrefs().getString("timer_length", "120"));
        int displayStrIndex = getTimerIndexValueFor(selectedTime);

        String[] displayStrings = getResources().getStringArray(R.array.time_limit_entries);
        String selectedTimeDisplayStr = displayStrIndex == -1 ? "" :  displayStrings[displayStrIndex];
        return new TimerLength(selectedTime, selectedTimeDisplayStr);
    }


    private int getTimerIndexValueFor(int selectedTime){
        String[] timerValues = getResources().getStringArray(R.array.time_limit_values);
        for (int i =0; i< timerValues.length; i++){
            int timerValue = Integer.parseInt(timerValues[i]);
            if( timerValue == selectedTime){
                return i;
            }
        }
        return -1;
    }


    public void setDifficulty(int gameLevel){
        reassignActivityToService();
        gameService.setGameLevel(gameLevel);
    }


    private void reassignActivityToService(){
        if(gameService == null){
            return;
        }
        if(gameService.isActivityUnbound()){
            gameService.setActivity(MainActivity.this);
        }
    }


    public <E extends Enum<E>> void sendMessage(E operationName, Bundle bundle){
        getSupportFragmentManager().setFragmentResult(operationName.toString(), bundle );
    }


}