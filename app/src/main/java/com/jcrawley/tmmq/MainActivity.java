package com.jcrawley.tmmq;

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
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.GameOverScreenFragment;
import com.jcrawley.tmmq.view.fragments.GameScreenFragment;
import com.jcrawley.tmmq.view.fragments.WelcomeScreenFragment;


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
        setupFragments();
        setupGameService();
     }


    public void assignVibrationSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isVibrationEnabled = prefs.getBoolean("vibration_enabled", true);
    }


    private void setupFragments(){
        fragmentContainerView = findViewById(R.id.fragment_container);
        Fragment welcomeScreenFragment = new WelcomeScreenFragment();
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
        bundle.putString(GameScreenFragment.QUESTION_TAG, questionText);
        getSupportFragmentManager().setFragmentResult(GameScreenFragment.SET_QUESTION, bundle );
    }


    public void setQuestionText(String questionText){
         Bundle bundle = new Bundle();
        bundle.putString(GameScreenFragment.QUESTION_TAG, questionText);
        getSupportFragmentManager().setFragmentResult(GameScreenFragment.SET_QUESTION, bundle );
    }


    private void setupGameService(){
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void setTimeRemaining(int minutesRemaining, int secondsRemaining){
        Bundle bundle = new Bundle();
        bundle.putInt(GameScreenFragment.MINUTES_REMAINING_TAG, minutesRemaining);
        bundle.putInt(GameScreenFragment.SECONDS_REMAINING_TAG, secondsRemaining);
        getSupportFragmentManager().setFragmentResult(GameScreenFragment.SET_TIME_REMAINING, bundle);
    }


    public void onGameOver(int finalScore){
         Bundle bundle = new Bundle();
         bundle.putInt(GameOverScreenFragment.FINAL_SCORE_KEY, finalScore);
         getSupportFragmentManager().setFragmentResult(GameScreenFragment.NOTIFY_GAME_OVER, bundle);
    }


    public void submitAnswer(){
        if(viewModel.currentAnswerText.trim().isEmpty()){
            return;
        }
        submitAnswer(viewModel.currentAnswerText);
    }


    public void setScore(int score){
         Bundle bundle = new Bundle();
         bundle.putInt(GameScreenFragment.SCORE_TAG, score);
         getSupportFragmentManager().setFragmentResult(GameScreenFragment.SET_SCORE, bundle);
    }


    private void submitAnswer(String answerStr){
        reassignActivityToService();
        gameService.submitAnswer(answerStr);
    }


    public void startGame(){
        reassignActivityToService();
        assignVibrationSettings();
        gameService.startGame();
    }


    public void setDifficulty(int gameLevel){
        reassignActivityToService();
        gameService.setGameLevel(gameLevel);
    }


    private void reassignActivityToService(){
        if(gameService.isActivityUnbound()){
            gameService.setActivity(MainActivity.this);
        }
    }

}