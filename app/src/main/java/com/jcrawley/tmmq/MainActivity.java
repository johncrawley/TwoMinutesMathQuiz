package com.jcrawley.tmmq;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.GameOverScreenFragment;
import com.jcrawley.tmmq.view.fragments.GameScreenFragment;
import com.jcrawley.tmmq.view.fragments.WelcomeScreenFragment;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private GameService gameService;
    private final AtomicBoolean isGameStarted = new AtomicBoolean(false);
    private MainViewModel viewModel;
    private Vibrator vibrator;
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


     private void setupFragments(){
        fragmentContainerView = findViewById(R.id.fragment_container);
        Fragment welcomeScreenFragment = new WelcomeScreenFragment();
        getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, welcomeScreenFragment)
        .commit();
     }


     public void resetStartGameScreen(){
        isGameStarted.set(false);
     }


     private void updateGameViewsFromService(){
         reassignActivityToService();
         setTimeRemaining(gameService.getMinutesRemaining(), gameService.getSecondsRemaining());
         setScore(gameService.getScore());
         setQuestionText(gameService.getQuestionText());
     }

/*
     private void setupScreenViews(){
         startScreenLayout = setupScreenView(R.id.startGameLayoutInclude, viewModel.startScreenVisibility);
         gameScreenLayout = setupScreenView(R.id.gameLayoutInclude, viewModel.gameScreenVisibility);
         gameOverScreenLayout = setupScreenView(R.id.gameOverLayoutInclude, viewModel.gameOverScreenVisibility);
     }


 */


     private ViewGroup setupScreenView(int id, int initialVisibility){
        ViewGroup viewGroup = findViewById(id);
        viewGroup.setVisibility(initialVisibility);
        return viewGroup;
     }


     private void setupVibe(){
        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
     }


     public void vibrateOnPress(){
         if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             vibrator.vibrate(VibrationEffect.createOneShot(55, 1));
         }
     }


     public MainViewModel getViewModel(){
        return viewModel;
     }


     private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
     }


    public void fadeInQuestionText(String questionText){
         log("entered fadeInQuestionText, text: " + questionText);
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


    private int getColorFromAttribute(int attr){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
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


    public void updateGameStartCountdownText(){
      // waiting for deletion!
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
      gameService.startGame();
    }


    private void reassignActivityToService(){
        if(gameService.isActivityUnbound()){
            gameService.setActivity(MainActivity.this);
        }
    }

}