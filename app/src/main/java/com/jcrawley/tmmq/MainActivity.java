package com.jcrawley.tmmq;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.TextAnimator;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, timeRemainingTextView, scoreView;
    private boolean bound = false;
    private GameService gameService;
    private final AtomicBoolean isGameStarted = new AtomicBoolean(false);
    private ViewGroup startGameScreen;
    private MainViewModel viewModel;
    private TextAnimator textAnimator;
    private Vibrator vibrator;



    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            log("Entered onServiceDisconnected()");
            bound = false;
        }
    };


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupViewModel();
        new InputHelper(this);
        setupGameService();
        setupStartButton();
        setupVibe();
     }


     private void setupViews(){
         questionTextView = findViewById(R.id.questionText);
         textAnimator = new TextAnimator(questionTextView);
         timeRemainingTextView = findViewById(R.id.timeRemainingText);
         startGameScreen = findViewById(R.id.startGameScreenInclude);
         scoreView = findViewById(R.id.scoreText);
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


    public void setQuestionText(String questionText){
        textAnimator.setNextText(questionText);
        questionTextView.startAnimation(textAnimator.getFadeOutAnimation());
    }


    private void setupGameService(){
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        runOnUiThread(()->{
            timeRemainingTextView.setText(createTimeRemainingString(minutesRemaining, secondsRemaining));
        });
    }


    private String createTimeRemainingString(int minutesRemaining, int secondsRemaining){
        String delimiter = ":";
        String displaySeconds = secondsRemaining < 10 ? "0" + secondsRemaining : String.valueOf(secondsRemaining);
        return minutesRemaining + delimiter + displaySeconds;
    }


    private void setupStartButton(){
        Button button = findViewById(R.id.startGameButton);
        button.setOnClickListener(v -> {
            if(isGameStarted.get()){
                return;
            }
            startGameScreen.setVisibility(View.INVISIBLE);
            gameService.startGame();
            isGameStarted.set(true);
        });
    }


    public void submitAnswer(){
        if(viewModel.currentAnswerText.trim().isEmpty()){
            return;
        }
        gameService.submitAnswer(viewModel.currentAnswerText);
    }


    public void updateScore(int score){
        String scoreStr = getString(R.string.score_label) + score;
        scoreView.setText(scoreStr);
    }


}