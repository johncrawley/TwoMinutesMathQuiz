package com.jcrawley.tmmq;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.StartScreenAnimator;
import com.jcrawley.tmmq.view.TextAnimator;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, timeRemainingTextView, scoreView;
    private boolean bound = false;
    private GameService gameService;
    private final AtomicBoolean isGameStarted = new AtomicBoolean(false);
    private ViewGroup startGameScreen, gameScreen;
    private MainViewModel viewModel;
    private TextAnimator textAnimator;
    private Vibrator vibrator;
    private Button gameStartButton;
    private TextView gameStartCountdownText;
    private StartScreenAnimator startScreenAnimator;



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
        startScreenAnimator = new StartScreenAnimator(MainActivity.this);
        new InputHelper(this);
        setupGameService();
        setupStartButton();
        setupVibe();
     }


     private void setupViews(){
         questionTextView = findViewById(R.id.questionText);
         textAnimator = new TextAnimator(questionTextView);
         timeRemainingTextView = findViewById(R.id.timeRemainingText);
         startGameScreen = findViewById(R.id.startGameLayoutInclude);
         scoreView = findViewById(R.id.scoreText);
         gameStartCountdownText = findViewById(R.id.gameStartCountdownText);
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
        runOnUiThread(()->{
            textAnimator.setNextText(questionText);
            questionTextView.startAnimation(textAnimator.getFadeOutAnimation());
        });
    }


    public void setQuestionText(String questionText){
        runOnUiThread(()->{
            questionTextView.setText(questionText);
        });
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
        gameStartButton = findViewById(R.id.startGameButton);
        gameStartButton.setOnClickListener(v -> {
            if(isGameStarted.get()){
                return;
            }
            startScreenAnimator.beginGameStartAnimations(gameStartButton);
            isGameStarted.set(true);
        });
    }



    public void updateGameStartCountdownText(){
        runOnUiThread(()->{
            gameStartCountdownText.setText(String.valueOf(viewModel.gameStartCurrentCountdown));
        });
    }


    public void submitAnswer(){
        if(viewModel.currentAnswerText.trim().isEmpty()){
            return;
        }
        submitAnswer(viewModel.currentAnswerText);
    }


    public void updateScore(int score){
        String scoreStr = getString(R.string.score_label) + score;
        scoreView.setText(scoreStr);
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