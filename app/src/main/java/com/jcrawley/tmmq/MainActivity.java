package com.jcrawley.tmmq;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
    private Button gameStartButton;
    private TextView gameStartCountdownText;



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
        gameStartButton = findViewById(R.id.startGameButton);
        gameStartButton.setOnClickListener(v -> {
            if(isGameStarted.get()){
                return;
            }
            beginGameStartAnimations();
            isGameStarted.set(true);
        });
    }


    private void beginGameStartAnimations(){
        Animation startButtonFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        startButtonFadeOutAnimation.setDuration(300);
        startButtonFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                gameStartButton.setVisibility(View.GONE);
                updateGameStartCountdownText();
                viewModel.gameStartCurrentCountdown = viewModel.gameStartInitialCountdown;
               // startGameCountdownTextAnimation();
                startTextAnimation(gameStartCountdownText, 500,500);

            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
        gameStartButton.startAnimation(startButtonFadeOutAnimation);
    }


    public void startTextAnimation(final TextView v, int enlargeTime, int reductionTime){
        final AnimationSet animationSet = new AnimationSet(true);

        Animation enlargeAnimation = new ScaleAnimation(1.0f,2f,1.0f,2f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        enlargeAnimation.setFillAfter(true);
        enlargeAnimation.setDuration(enlargeTime);

        Animation reductionAnimation = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        reductionAnimation.setFillAfter(true);
        reductionAnimation.setDuration(reductionTime);
        reductionAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation){ v.setVisibility(View.VISIBLE);}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(viewModel.gameStartCurrentCountdown > 1){
                    viewModel.gameStartCurrentCountdown--;
                    updateGameStartCountdownText();
                    v.startAnimation(animationSet);
                }
                else{
                    log("entered else");
                    fadeOutStartScreen(startGameScreen);
                }
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });

        animationSet.addAnimation(enlargeAnimation);
        animationSet.addAnimation(reductionAnimation);
        v.startAnimation(animationSet);
    }


    private void fadeOutStartScreen(final ViewGroup startScreen){

        TranslateAnimation animation = new TranslateAnimation(0, 0,0, 3000);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation) {
                gameService.startGame();
                startScreen.setVisibility(View.GONE);
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });
        startScreen.startAnimation(animation);
    }


    private void updateGameStartCountdownText(){
        runOnUiThread(()->{
            gameStartCountdownText.setText(String.valueOf(viewModel.gameStartCurrentCountdown));
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