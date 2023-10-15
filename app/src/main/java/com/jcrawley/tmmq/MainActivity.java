package com.jcrawley.tmmq;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.MainViewModel;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, timeRemainingTextView, scoreView;
    private boolean bound = false;
    private GameService gameService;
    private final AtomicBoolean isGameStarted = new AtomicBoolean(false);
    private ViewGroup startGameScreen;
    private MainViewModel viewModel;
    private InputHelper inputHelper;



    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            log("onServiceConnected() created an instance of GameService.LocalBinder");
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupViewModel();
        inputHelper = new InputHelper(this);
        setupGameService();
        setupStartButton();
     }

     private void setupViews(){
         questionTextView = findViewById(R.id.questionText);
         timeRemainingTextView = findViewById(R.id.timeRemainingText);
         startGameScreen = findViewById(R.id.startGameScreenInclude);
         scoreView = findViewById(R.id.scoreText);
     }


     public MainViewModel getViewModel(){
        return viewModel;
     }


     private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
     }


    public void setQuestionText(String questionText){
       questionTextView.setText(questionText);
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
        gameService.submitAnswer(viewModel.currentAnswerText);
    }


    public void updateScore(int score){
        String scoreStr = getString(R.string.score_label) + score;
        scoreView.setText(scoreStr);
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity : " + msg);
    }


}