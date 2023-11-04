package com.jcrawley.tmmq.view.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.InputHelper;


public class GameScreenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SET_TIME_REMAINING = "send_time_remaining";
    public static final String SET_SCORE = "set_score";
    public static final String SET_QUESTION = "set_question";
    public static final String MINUTES_REMAINING_TAG = "minutes_remaining";
    public static final String SECONDS_REMAINING_TAG = "seconds_remaining";
    private TextView timeRemainingTextView, scoreTextView, questionTextView;
    int timeRemainingTextNormalColor, timeRemainingTextWarningColor;


    public GameScreenFragment() {
        // Required empty public constructor
    }



    public static GameScreenFragment newInstance(String param1, String param2) {
        GameScreenFragment fragment = new GameScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_game_screen, container, false);
        if(getActivity() == null){
            return parentView;
        }
        setupColors();
        setupViews(parentView);
        new InputHelper((MainActivity) getActivity(), parentView);
        return parentView;
    }


    private void setupListeners(){
        FragmentManagerHelper.setListener(this, SET_TIME_REMAINING, this::updateTimeRemaining);
        FragmentManagerHelper.setListener(this, SET_SCORE, this::setScore);
        FragmentManagerHelper.setListener(this, SET_QUESTION, this::setQuestion);

    }


    private void setupViews(View parentView){
        timeRemainingTextView = parentView.findViewById(R.id.timeRemainingText);
        scoreTextView = parentView.findViewById(R.id.scoreText);
        questionTextView = parentView.findViewById(R.id.questionText);
    }


    private void updateTimeRemaining(Bundle bundle){
        int minutesRemaining =  bundle.getInt(MINUTES_REMAINING_TAG);
        int secondsRemaining = bundle.getInt(SECONDS_REMAINING_TAG);

        runOnUiThread(()->{
            setTimeRemainingTextColor(minutesRemaining, secondsRemaining);
            timeRemainingTextView.setText(createTimeRemainingString(minutesRemaining, secondsRemaining));
        });
    }


    private String createTimeRemainingString(int minutesRemaining, int secondsRemaining){
        String delimiter = ":";
        String displaySeconds = secondsRemaining < 10 ? "0" + secondsRemaining : String.valueOf(secondsRemaining);
        return minutesRemaining + delimiter + displaySeconds;
    }


    private void setTimeRemainingTextColor(int minutesRemaining, int secondsRemaining){
        int textColor = minutesRemaining == 0 && secondsRemaining < 10 ?
                timeRemainingTextWarningColor : timeRemainingTextNormalColor;
        timeRemainingTextView.setTextColor(textColor);
    }


    private void setScore(Bundle bundle){

    }


    private void setQuestion(Bundle bundle){

    }


    private void runOnUiThread(Runnable runnable){
        Activity activity = getActivity();
        if(activity == null){
            return;
        }
        activity.runOnUiThread(runnable);
    }



    private void setupColors(){
        timeRemainingTextNormalColor = getColorFromAttribute(R.attr.time_remaining_text_normal_color);
        timeRemainingTextWarningColor = getColorFromAttribute(R.attr.time_remaining_text_warning_color);
    }


    private int getColorFromAttribute(int attr){
        Activity activity = getActivity();
        if(activity == null){
            return Color.BLACK;
        }
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }


}