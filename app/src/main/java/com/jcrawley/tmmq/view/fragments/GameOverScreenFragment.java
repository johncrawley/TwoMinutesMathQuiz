package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.R;


public class GameOverScreenFragment extends Fragment {


    public static final String FRAGMENT_TAG = "game_over_screen";
    public static String FINAL_SCORE_KEY = "final_score_key";
    public static String TIMER_LENGTH_KEY = "timer_length_key";
    public static String DAILY_HIGH_SCORE_KEY = "daily_high_score_key";
    public static String ALL_TIME_HIGH_SCORE_KEY = "all_time_high_score_key";
    public static String GAME_LEVEL_KEY = "game_level_key";

    private int finalScore, dailyHighScore, allTimeHighScore;
    private String timerLength, gameLevel;

    public GameOverScreenFragment() {
        // Required empty public constructor
    }


    public static GameOverScreenFragment newInstance() {
       return new GameOverScreenFragment();
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
        setupStartNewGameButton(parentView);
        FragmentUtils.onBackButtonPressed(this, this::loadGetReadyFragment);
        return parentView;
    }


    private void loadGetReadyFragment(){
        GetReadyScreenFragment getReadyFragment = new GetReadyScreenFragment();
        FragmentUtils.loadFragment(this, getReadyFragment, GetReadyScreenFragment.FRAGMENT_TAG);
    }


    private void setupFinalScoreText(View parentView){
        TextView finalScoreTextView = parentView.findViewById(R.id.endingScoreText);
        finalScoreTextView.setText(String.valueOf(finalScore));
    }


    private void setupStartNewGameButton(View parentView){
        Button button = parentView.findViewById(R.id.mainMenuButton);
        button.setOnClickListener(v -> startWelcomeScreenFragment());
    }


    private void startWelcomeScreenFragment(){
        FragmentUtils.loadFragment(this, new WelcomeScreenFragment(), WelcomeScreenFragment.FRAGMENT_TAG);
    }
}