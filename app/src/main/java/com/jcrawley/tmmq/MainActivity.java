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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.ScreenAnimator;
import com.jcrawley.tmmq.view.TextAnimator;
import com.jcrawley.tmmq.view.fragments.WelcomeScreenFragment;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, timeRemainingTextView, scoreView;
    private GameService gameService;
    private final AtomicBoolean isGameStarted = new AtomicBoolean(false);
    private MainViewModel viewModel;
    private TextAnimator textAnimator;
    private Vibrator vibrator;
    private TextView getReadyText;
    private TextView gameStartCountdownText;
   // private ScreenAnimator screenAnimator;
    private TextView endingScoreText;
  //  private ViewGroup gameScreenLayout, gameOverScreenLayout, startScreenLayout;
    private ViewGroup startGameButtonsLayout;
    int timeRemainingTextNormalColor, timeRemainingTextWarningColor;
    private FragmentContainerView fragmentContainerView;



    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            updateCurrentStatusFromService();
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
        setupFragments();
       // setupColors();
       // setupViews();
       // screenAnimator = new ScreenAnimator(MainActivity.this);
       // new InputHelper(this);
        setupGameService();
       // setupVibe();
     }


     private void setupViews(){
        // setupScreenViews();
         questionTextView = findViewById(R.id.questionText);
         textAnimator = new TextAnimator(questionTextView);
         timeRemainingTextView = findViewById(R.id.timeRemainingText);
         endingScoreText = findViewById(R.id.endingScoreText);
         scoreView = findViewById(R.id.scoreText);
         getReadyText = findViewById(R.id.getReadyText);
         gameStartCountdownText = findViewById(R.id.gameStartCountdownText);
         setupStartButton();
         setupNewGameButton();
     }


     private void setupFragments(){
        fragmentContainerView = findViewById(R.id.fragment_container);
        Fragment welcomeScreenFragment = new WelcomeScreenFragment();
        getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, welcomeScreenFragment)
        .commit();
     }


     public void resetStartGameScreen(){
        startGameButtonsLayout.setVisibility(View.VISIBLE);
        getReadyText.setVisibility(View.INVISIBLE);
        gameStartCountdownText.setVisibility(View.GONE);
        isGameStarted.set(false);
     }


     public void updateCurrentStatusFromService(){
        log("Entered updateCurrentStatusFromService()");
        if(gameService.isGameStarted()){
            setGameOverScreenVisibility(View.GONE);
            setGameScreenVisibility(View.VISIBLE);
            setStartScreenVisibility(View.GONE);
            updateGameViewsFromService();
            return;
        }
        setGameOverScreenVisibility(View.GONE);
        setGameScreenVisibility(View.GONE);
        setStartScreenVisibility(View.VISIBLE);
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


    private void setupColors(){
        timeRemainingTextNormalColor = getColorFromAttribute(R.attr.time_remaining_text_normal_color);
        timeRemainingTextWarningColor = getColorFromAttribute(R.attr.time_remaining_text_warning_color);
    }


    private int getColorFromAttribute(int attr){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }


    public void setTimeRemaining(int minutesRemaining, int secondsRemaining){
        runOnUiThread(()->{
            setTimeRemainingTextColor(minutesRemaining, secondsRemaining);
            timeRemainingTextView.setText(createTimeRemainingString(minutesRemaining, secondsRemaining));
        });
    }


    private void setTimeRemainingTextColor(int minutesRemaining, int secondsRemaining){
        int textColor = minutesRemaining == 0 && secondsRemaining < 10 ?
                timeRemainingTextWarningColor : timeRemainingTextNormalColor;
        timeRemainingTextView.setTextColor(textColor);
    }


    public void onGameOver(int finalScore){
        runOnUiThread(()->{
            endingScoreText.setText(String.valueOf(finalScore));
        //    screenAnimator.fadeInGameOverScreen();
        });
    }


    private String createTimeRemainingString(int minutesRemaining, int secondsRemaining){
        String delimiter = ":";
        String displaySeconds = secondsRemaining < 10 ? "0" + secondsRemaining : String.valueOf(secondsRemaining);
        return minutesRemaining + delimiter + displaySeconds;
    }


    private void setupStartButton(){
        Button gameStartButton = findViewById(R.id.startGameButton);
        startGameButtonsLayout = findViewById(R.id.startButtonsLayout);
        gameStartButton.setOnClickListener(v -> {
            if(isGameStarted.get()){
                return;
            }
            updateGameViewsFromService();
            questionTextView.setText("");
          //  screenAnimator.fadeInView(getReadyText);
            //  screenAnimator.beginGameStartAnimations(startGameButtonsLayout);
            isGameStarted.set(true);
        });
    }


    private void setupNewGameButton(){
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(V -> {
          //  screenAnimator.hideGameOverScreen();
              });
    }


    public void setStartScreenVisibility(int visibility){
        viewModel.startScreenVisibility = visibility;
       // startScreenLayout.setVisibility(viewModel.startScreenVisibility);
    }


    public void setGameScreenVisibility(int visibility){
        viewModel.gameScreenVisibility = visibility;
       // gameScreenLayout.setVisibility(viewModel.gameScreenVisibility);
    }


    public void setGameOverScreenVisibility(int visibility){
        viewModel.gameOverScreenVisibility = visibility;
       // gameOverScreenLayout.setVisibility(viewModel.gameOverScreenVisibility);
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


    public void setScore(int score){
        String scoreStr = getString(R.string.score_label) + score;
        runOnUiThread(()->{
            scoreView.setText(scoreStr);
        });
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