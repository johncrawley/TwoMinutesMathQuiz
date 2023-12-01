package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.TextAnimator;


public class GameScreenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SET_TIME_REMAINING = "send_time_remaining";
    public static final String NOTIFY_GAME_OVER = "notify_game_over";
    public static final String SET_SCORE = "set_score";
    public static final String SET_QUESTION = "set_question";
    public static final String MINUTES_REMAINING_TAG = "minutes_remaining_tag";
    public static final String SECONDS_REMAINING_TAG = "seconds_remaining_tag";
    public static final String SCORE_TAG = "score_tag";
    public static final String QUESTION_TAG = "question_tag";
    public static final String WAS_ANSWER_CORRECT_TAG = "was_answer_correct_tag";
    public static final String WAS_ANSWER_INCORRECT_TAG = "was_answer_incorrect_tag";
    private TextView timeRemainingTextView, scoreTextView, questionTextView;
    private int timeRemainingTextNormalColor, timeRemainingTextWarningColor;
    private TextAnimator textAnimator;
    private GameScreenViewModel viewModel;
    private InputHelper inputHelper;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_game_screen, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity == null){
            return parentView;
        }
        setupColors();
        setupViewModel();
        setupViews(parentView);
        inputHelper = new InputHelper(mainActivity, parentView, viewModel);
        mainActivity.startGame();
        setupListeners();
        FragmentUtils.onBackButtonPressed(this, this::stopTimerAndReturnToWelcomeScreen);
        return parentView;
    }


    private void setupViewModel(){
        viewModel = new ViewModelProvider(requireActivity()).get(GameScreenViewModel.class);
    }


    private void setupListeners(){
        FragmentUtils.setListener(this, SET_TIME_REMAINING, this::updateTimeRemaining);
        FragmentUtils.setListener(this, SET_SCORE, this::setScore);
        FragmentUtils.setListener(this, SET_QUESTION, this::setQuestion);
        FragmentUtils.setListener(this, NOTIFY_GAME_OVER, this::onGameOver);
    }


    private void setupViews(View parentView){
        timeRemainingTextView = setupTextView(parentView, R.id.timeRemainingText, viewModel.timeRemaining);
        scoreTextView = setupTextView(parentView, R.id.scoreText, createScoreString(viewModel.scoreValue));
        questionTextView = setupTextView(parentView, R.id.questionText, viewModel.questionText);
        textAnimator = new TextAnimator(questionTextView, getColorFromAttribute(R.attr.default_question_text_color, getContext()));
    }


    private TextView setupTextView(View parentView, int id, String viewModelValue){
        TextView textView = parentView.findViewById(id);
        textView.setText(viewModelValue);
        return textView;
    }


    private void updateTimeRemaining(Bundle bundle){
        if(timeRemainingTextView == null){
            return;
        }
        int minutesRemaining = bundle.getInt(MINUTES_REMAINING_TAG);
        int secondsRemaining = bundle.getInt(SECONDS_REMAINING_TAG);
        log("Entered updateTimeRemaining()");
        runOnUiThread(()->{
            timeRemainingTextView.setVisibility(View.VISIBLE);
            setTimeRemainingTextColor(minutesRemaining, secondsRemaining);
            viewModel.timeRemaining = createTimeRemainingString(minutesRemaining, secondsRemaining);;
            timeRemainingTextView.setText(viewModel.timeRemaining);
        });
    }


    private String createTimeRemainingString(int minutesRemaining, int secondsRemaining){
        String delimiter = ":";
        String displaySeconds = secondsRemaining < 10 ? "0" + secondsRemaining : String.valueOf(secondsRemaining);
        return minutesRemaining + delimiter + displaySeconds;
    }


    private void setTimeRemainingTextColor(int minutesRemaining, int secondsRemaining){
        int warningTime = getResources().getInteger(R.integer.countdown_warning_time);
        int totalSecondsRemaining = (minutesRemaining * 60) + secondsRemaining;
        timeRemainingTextView.setTextColor(totalSecondsRemaining <= warningTime ?
                timeRemainingTextWarningColor : timeRemainingTextNormalColor);

        if(totalSecondsRemaining == warningTime){
            animateTextViewForWarning();
        }
    }


    private void animateTextViewForWarning(){
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(timeRemainingTextView, "backgroundColor",
                Color.RED, Color.TRANSPARENT);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(300);
        colorAnim.start();
    }


    private void setScore(Bundle bundle){
        viewModel.scoreValue = bundle.getInt(SCORE_TAG);;
        runOnUiThread(()-> scoreTextView.setText(createScoreString(viewModel.scoreValue)));
    }


    private String createScoreString(int score){
        return getString(R.string.score_label) + score;
    }


    private void setQuestion(Bundle bundle){
        if(bundle.getBoolean(WAS_ANSWER_CORRECT_TAG, false)){

        }
        else if(bundle.getBoolean(WAS_ANSWER_INCORRECT_TAG, false)){

        }
       fadeInNewQuestionText(bundle);
    }

    private void fadeInNewQuestionText(Bundle bundle){
        String text = bundle.getString(QUESTION_TAG);
        runOnUiThread(()-> {
            textAnimator.setNextText(text);
            viewModel.questionText = text;
            questionTextView.startAnimation(textAnimator.getFadeOutAnimation());
        });
    }


    private void log(String msg){
        System.out.println("^^^ GameScreenFragment " +  msg);
    }


    private void onGameOver(Bundle bundle){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity != null) {
            mainActivity.notifyServiceThatGameHasFinished();
        }
        runOnUiThread(()->inputHelper.clearAnswerText());
        GameOverScreenFragment gameOverScreenFragment = new GameOverScreenFragment();
        FragmentUtils.loadFragment(this, gameOverScreenFragment, GameOverScreenFragment.FRAGMENT_TAG, bundle);
        resetViewData();
    }


    private void stopTimerAndReturnToWelcomeScreen(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.stopGame();
        }
        loadWelcomeScreen();
    }


    private void runOnUiThread(Runnable runnable){
        Activity activity = getActivity();
        if(activity == null){
            return;
        }
        activity.runOnUiThread(runnable);
    }


    private void loadWelcomeScreen(){
        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentUtils.loadFragment(this, welcomeScreenFragment, WelcomeScreenFragment.FRAGMENT_TAG);
    }


    private void resetViewData(){
        viewModel.scoreValue = 0;
        viewModel.inputText = "";
        viewModel.timeRemaining = "";
        viewModel.questionText = "";
    }


    private void setupColors(){
        timeRemainingTextNormalColor = getColorFromAttribute(R.attr.time_remaining_text_normal_color, getContext());
        timeRemainingTextWarningColor = getColorFromAttribute(R.attr.time_remaining_text_warning_color, getContext());
    }



}