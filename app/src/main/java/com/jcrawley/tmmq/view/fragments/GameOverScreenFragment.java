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


    private int finalScore;
    public static String FINAL_SCORE_KEY = "final_score_key";


    public GameOverScreenFragment() {
        // Required empty public constructor
    }


    public static GameOverScreenFragment newInstance(String param1, String param2) {
        GameOverScreenFragment fragment = new GameOverScreenFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            finalScore = getArguments().getInt(FINAL_SCORE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_game_over_screen, container, false);
        setupFinalScoreText(parentView);
        setupStartNewGameButton(parentView);
        return parentView;
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