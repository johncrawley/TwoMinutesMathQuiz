package com.jcrawley.tmmq.view.fragments.game;

import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.NOTIFY_GAME_OVER;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.NOTIFY_INCORRECT_ANSWER;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.SET_TIME_REMAINING;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.animateTextColor;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.getInt;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.getStr;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.TextAnimator;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;
import com.jcrawley.tmmq.view.fragments.GameOverFragment;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;


public class GameFragment extends Fragment {

    public enum Message {SET_TIME_REMAINING, NOTIFY_GAME_OVER, NOTIFY_INCORRECT_ANSWER, SET_SCORE, SET_QUESTION  }
    public enum Tag { MINUTES_REMAINING, SECONDS_REMAINING, SCORE, QUESTION}
    private TextView timeRemainingTextView, scoreTextView, questionTextView, inputTextView;
    private int timeRemainingTextNormalColor, timeRemainingTextWarningColor;
    private int defaultAnswerTextColor;
    private TextAnimator textAnimator;
    private GameScreenViewModel viewModel;
    private InputHelper inputHelper;


    public GameFragment() {
        // Required empty public constructor
    }


    public static GameFragment newInstance() {
        return new GameFragment();
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
        FragmentUtils.setListener(this, SET_TIME_REMAINING.toString(), this::updateTimeRemaining);
        FragmentUtils.setListener(this, Message.SET_SCORE.toString(), this::setScore);
        FragmentUtils.setListener(this, Message.SET_QUESTION.toString(), this::setQuestion);
        FragmentUtils.setListener(this, NOTIFY_GAME_OVER.toString(), this::onGameOver);
        FragmentUtils.setListener(this, NOTIFY_INCORRECT_ANSWER.toString(), this::onIncorrectAnswer);
    }


    private void onIncorrectAnswer(Bundle bundle){
        int incorrectColor = getColorFromAttribute(R.attr.incorrect_answer_text_color, getContext());
        int initialDelay = 0;
        int duration = 150;
        animateTextColor(inputTextView, defaultAnswerTextColor, incorrectColor, initialDelay, duration);
        clearAnswerTextAfterDelay(initialDelay + duration + 100);
    }


    public void clearAnswerTextAfterDelay(int delay){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
                    viewModel.inputText = "";
                    inputTextView.setText(viewModel.inputText);
                    inputTextView.setTextColor(defaultAnswerTextColor);
                },delay);
    }


    private void setupViews(View parentView){
        timeRemainingTextView = setupTextView(parentView, R.id.timeRemainingText, viewModel.timeRemaining);
        scoreTextView = setupTextView(parentView, R.id.scoreText, createScoreString(viewModel.scoreValue));
        questionTextView = setupTextView(parentView, R.id.questionText, viewModel.questionText);
        inputTextView = setupTextView(parentView, R.id.inputText, viewModel.questionText);
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
        int minutesRemaining = getInt(bundle, Tag.MINUTES_REMAINING);
        int secondsRemaining = getInt(bundle, Tag.SECONDS_REMAINING);
        runOnUiThread(()-> updateTimeTextView(minutesRemaining, secondsRemaining));
    }


    private void updateTimeTextView(int minutesRemaining, int secondsRemaining){
        timeRemainingTextView.setVisibility(View.VISIBLE);
        setTimeRemainingTextColor(minutesRemaining, secondsRemaining);
        viewModel.timeRemaining = createTimeRemainingString(minutesRemaining, secondsRemaining);;
        timeRemainingTextView.setText(viewModel.timeRemaining);
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


    private void animateScoreOnUpdate(){
        int normalColor = getColorFromAttribute(R.attr.score_normal_color, getContext());
        int flashColor = getColorFromAttribute(R.attr.score_flash_color, getContext());
        animateTextColor(scoreTextView, normalColor, flashColor, 10, 80);
        animateTextColor(scoreTextView, flashColor, normalColor, 200, 150);
    }


    private void setScore(Bundle bundle){
        viewModel.scoreValue = getInt(bundle, Tag.SCORE);;
        runOnUiThread(()-> {
            scoreTextView.setText(createScoreString(viewModel.scoreValue));
            animateScoreOnUpdate();
            int correctAnswerTextColor = getColorFromAttribute(R.attr.correct_answer_text_color, getContext());
            int colorChangeDuration = 150;
            animateTextColor(inputTextView, defaultAnswerTextColor, correctAnswerTextColor, 0, colorChangeDuration);
            clearAnswerTextAfterDelay(colorChangeDuration + 200);
        });
    }


    private String createScoreString(int score){
        return String.valueOf(score);
    }


    private void setQuestion(Bundle bundle){
       fadeInNewQuestionText(bundle);
    }


    private void fadeInNewQuestionText(Bundle bundle){
        String text = getStr(bundle, Tag.QUESTION);
        runOnUiThread(()-> {
            textAnimator.setNextText(text);
            viewModel.questionText = text;
            questionTextView.startAnimation(textAnimator.getFadeAnimation());
        });
    }


    private void onGameOver(Bundle bundle){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity != null) {
            mainActivity.notifyServiceThatGameHasFinished();
        }
        runOnUiThread(()->inputHelper.clearAnswerText());
        GameOverFragment gameOverFragment = new GameOverFragment();
        FragmentUtils.loadFragment(this, gameOverFragment, GameOverFragment.FRAGMENT_TAG, bundle);
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
        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        FragmentUtils.loadFragment(this, mainMenuFragment, MainMenuFragment.FRAGMENT_TAG);
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
        defaultAnswerTextColor = getColorFromAttribute(R.attr.default_answer_text_color, getContext());
    }



}