package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.view.fragments.utils.ActivityUtils.playSound;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.addGradientTo;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.loadFragment;
import static com.jcrawley.tmmq.view.fragments.utils.GeneralUtils.setTextForLandscape;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;


public class GameOverFragment extends Fragment {

    public static final String FRAGMENT_TAG = "game_over_screen";
    public static String FINAL_SCORE_KEY = "final_score_key";
    public static String TIMER_LENGTH_KEY = "timer_length_key";
    public static String DAILY_HIGH_SCORE_KEY = "daily_high_score_key";
    public static String ALL_TIME_HIGH_SCORE_KEY = "all_time_high_score_key";
    public static String GAME_LEVEL_KEY = "game_level_key";

    private int finalScore, dailyHighScore, allTimeHighScore;
    private String timerLength, gameLevel;
    private Button mainMenuButton, retryButton;
    private TextView gameOverText, gameOverTextShadow;

    public GameOverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assignDataFrom(getArguments());
        }
    }


    private void assignDataFrom(Bundle bundle){
        finalScore = bundle.getInt(FINAL_SCORE_KEY);
        dailyHighScore = bundle.getInt(DAILY_HIGH_SCORE_KEY);
        allTimeHighScore = bundle.getInt(ALL_TIME_HIGH_SCORE_KEY);
        timerLength = bundle.getString(TIMER_LENGTH_KEY);
        gameLevel = bundle.getString(GAME_LEVEL_KEY);
    }


    private void log(String msg){
        System.out.println("^^^ " + msg);
    }


    private void printInfo(){
        log("finalScore: " + finalScore);
        log(" dailyHighScore: " + dailyHighScore);
        log("all time highScore: " + allTimeHighScore);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_game_over, container, false);
        setupFinalScoreText(parentView);
        setupGameOverText(parentView);
        setupDetailsView(parentView);
        setupStartNewGameButton(parentView);
        setupRetryButton(parentView);
        FragmentUtils.onBackButtonPressed(this, this::loadGetReadyFragment);
        animateButtons(parentView);
        enableButtonsAfterDelay();
        return parentView;
    }


    private void setupGameOverText(View parentView){
        gameOverText = parentView.findViewById(R.id.gameOverText);
        gameOverTextShadow = parentView.findViewById(R.id.gameOverTextShadow);
        setTextForLandscape(this, R.string.game_over_text_landscape, gameOverText, gameOverTextShadow);
        assignGameOverMessage();
        addGradientTo(gameOverText, getContext());
    }


    private void assignGameOverMessage(){
        if(finalScore > allTimeHighScore){
            setGameOverText(getResources().getString(R.string.new_all_time_record));
        }
        else if(finalScore > dailyHighScore){
            setGameOverText(getResources().getString(R.string.new_daily_record));
        }
    }


    private void setGameOverText(String text){
        gameOverText.setText(text);
        gameOverTextShadow.setText(text);
    }


    private void animateButtons(View parentView){
        ViewGroup endGameButtonLayout = parentView.findViewById(R.id.endGameButtonLayout);
        endGameButtonLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up));
    }


    private void enableButtonsAfterDelay(){
        int delay = getResources().getInteger(R.integer.game_over_buttons_enable_delay);
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            mainMenuButton.setEnabled(true);
            retryButton.setEnabled(true);
            }, delay);
    }


    private void setupDetailsView(View parentView){
        TextView gameDetails = parentView.findViewById(R.id.gameDetailsText);

        String gameDetailsStr= getString(R.string.game_over_details, gameLevel, timerLength);
        gameDetails.setText(gameDetailsStr);
    }


    private void loadGetReadyFragment(){
        GetReadyFragment getReadyFragment = new GetReadyFragment();
        loadFragment(this, getReadyFragment, GetReadyFragment.FRAGMENT_TAG);
    }


    private void setupFinalScoreText(View parentView){
        TextView finalScoreTextView = parentView.findViewById(R.id.endingScoreText);
        finalScoreTextView.setText(String.valueOf(finalScore));
    }


    private void setupStartNewGameButton(View parentView){
        mainMenuButton = parentView.findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(v -> {
            playSound(this, Sound.MENU_BUTTON);
            startWelcomeScreenFragment();
        });
    }


    private void setupRetryButton(View parentView){
        retryButton = parentView.findViewById(R.id.retryMenuButton);
        retryButton.setOnClickListener(v ->{
            playSound(this, Sound.MENU_BUTTON);
            loadGetReadyFragment();
        });
    }


    private void startWelcomeScreenFragment(){
        loadFragment(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }
}