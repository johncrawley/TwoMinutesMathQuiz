package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.R;
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

    public GameOverFragment() {
        // Required empty public constructor
    }


    public static GameOverFragment newInstance() {
       return new GameOverFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }

        finalScore = bundle.getInt(FINAL_SCORE_KEY);
        dailyHighScore = bundle.getInt(DAILY_HIGH_SCORE_KEY);
        allTimeHighScore = bundle.getInt(ALL_TIME_HIGH_SCORE_KEY);
        timerLength = bundle.getString(TIMER_LENGTH_KEY);
        gameLevel = bundle.getString(GAME_LEVEL_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_game_over_screen, container, false);
        setupFinalScoreText(parentView);
        setupNewRecordView(parentView);
        setupDetailsView(parentView);
        setupStartNewGameButton(parentView);
        setupRetryButton(parentView);
        FragmentUtils.onBackButtonPressed(this, this::loadGetReadyFragment);
        return parentView;
    }


    private void setupNewRecordView(View parentView){
        TextView gameOverText = parentView.findViewById(R.id.gameOverText);
        if(finalScore > allTimeHighScore){
            gameOverText.setText(getResources().getString(R.string.new_all_time_record));
        }
        else if(finalScore > dailyHighScore){
            gameOverText.setText(getResources().getString(R.string.new_daily_record));
        }
    }


    private void setupDetailsView(View parentView){
        TextView gameDetails = parentView.findViewById(R.id.gameDetailsText);

        String gameDetailsStr= getString(R.string.game_over_details, gameLevel, timerLength);
        gameDetails.setText(gameDetailsStr);
    }


    private void loadGetReadyFragment(){
        GetReadyFragment getReadyFragment = new GetReadyFragment();
        FragmentUtils.loadFragment(this, getReadyFragment, GetReadyFragment.FRAGMENT_TAG);
    }


    private void setupFinalScoreText(View parentView){
        TextView finalScoreTextView = parentView.findViewById(R.id.endingScoreText);
        finalScoreTextView.setText(String.valueOf(finalScore));
    }


    private void setupStartNewGameButton(View parentView){
        Button button = parentView.findViewById(R.id.mainMenuButton);
        button.setOnClickListener(v -> startWelcomeScreenFragment());
    }


    private void setupRetryButton(View parentView){
        Button button = parentView.findViewById(R.id.retryMenuButton);
        button.setOnClickListener(v -> loadGetReadyFragment());
    }


    private void startWelcomeScreenFragment(){
        FragmentUtils.loadFragment(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }
}