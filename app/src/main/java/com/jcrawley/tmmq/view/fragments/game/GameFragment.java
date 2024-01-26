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
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.InputHelper;
import com.jcrawley.tmmq.view.TextAnimator;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;
import com.jcrawley.tmmq.view.fragments.GameOverFragment;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class GameFragment extends Fragment {

    public enum Message {SET_TIME_REMAINING, NOTIFY_GAME_OVER, NOTIFY_INCORRECT_ANSWER, SET_SCORE, SET_QUESTION, SHOW_QUESTION  }
    public enum Tag { MINUTES_REMAINING, SECONDS_REMAINING, SCORE, QUESTION}
    private TextView timeRemainingTextView, scoreTextView, questionTextView, inputTextView;
    private int timeRemainingTextNormalColor, timeRemainingTextWarningColor;
    private int defaultAnswerTextColor;
    private TextAnimator textAnimator;
    private GameScreenViewModel viewModel;
    private InputHelper inputHelper;
    private MainActivity mainActivity;
    private final AtomicBoolean isCreated = new AtomicBoolean(false);

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
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        mainActivity = (MainActivity) getActivity();
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
        isCreated.set(true);
        return parentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //isCreated.set(true);
    }


    private void setupViewModel(){
        viewModel = new ViewModelProvider(requireActivity()).get(GameScreenViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        isCreated.set(true);
    }


    private void setupListeners(){
        FragmentUtils.setListener(this, SET_TIME_REMAINING.toString(), b -> process(this::updateTimeRemaining, b));
        FragmentUtils.setListener(this, Message.SET_SCORE.toString(), b -> process(this::setScore, b));
        FragmentUtils.setListener(this, Message.SET_QUESTION.toString(), b -> process(this::setQuestion, b));
        FragmentUtils.setListener(this, NOTIFY_GAME_OVER.toString(), b -> process(this::onGameOver, b));
        FragmentUtils.setListener(this, NOTIFY_INCORRECT_ANSWER.toString(), b-> process(this::onIncorrectAnswer, b));
    }


    private void process(Consumer<Bundle> consumer, Bundle bundle){
        if(isCreated.get()){
            consumer.accept(bundle);
        }
    }

    private void onIncorrectAnswer(Bundle bundle){
        int incorrectColor = getColorFromAttribute(R.attr.incorrect_answer_text_color, getContext());
        int initialDelay = 0;
        int duration = 150;
        fadeOutQuestionText();
        animateTextColor(inputTextView, defaultAnswerTextColor, incorrectColor, initialDelay, duration);
        clearAnswerTextAfterDelay(initialDelay + duration + 300);
        playSoundWithDelay(Sound.INCORRECT_ANSWER);
        fadeInQuestionText(1000);
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


    private void playSound(Sound sound){
        if(mainActivity!= null){
            mainActivity.playSound(sound);
        }
    }


    private void playSoundWithDelay(Sound sound){
        if(mainActivity!= null){
            mainActivity.playSound(sound, 200);
        }
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
        int totalSecondsRemaining = (minutesRemaining * 60) + secondsRemaining;
        int warningTime = getResources().getInteger(R.integer.countdown_warning_time);
        timeRemainingTextView.setVisibility(View.VISIBLE);
        setTimeRemainingTextColor(totalSecondsRemaining, warningTime);
        handleWarningOnLowTime(totalSecondsRemaining, warningTime);
        viewModel.timeRemaining = createTimeRemainingString(minutesRemaining, secondsRemaining);;
        timeRemainingTextView.setText(viewModel.timeRemaining);
    }


    private String createTimeRemainingString(int minutesRemaining, int secondsRemaining){
        String delimiter = ":";
        String displaySeconds = secondsRemaining < 10 ? "0" + secondsRemaining : String.valueOf(secondsRemaining);
        return minutesRemaining + delimiter + displaySeconds;
    }


    private void setTimeRemainingTextColor(int totalSecondsRemaining, int warningTime){
        timeRemainingTextView.setTextColor(totalSecondsRemaining <= warningTime ?
                timeRemainingTextWarningColor : timeRemainingTextNormalColor);
    }


    private void handleWarningOnLowTime(int totalSecondsRemaining, int warningTime){
        if(totalSecondsRemaining == warningTime){
            animateTextViewForWarning();
            playSound(Sound.LOW_TIME);
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
        runOnUiThread(()-> {
            fadeOutQuestionText();
            updateScoreView(bundle);
            int colorChangeDuration = 150;
            changeInputTextColorForCorrectAnswer(colorChangeDuration);
            animateScoreOnUpdate();
            clearAnswerTextAfterDelay(colorChangeDuration + 200);
        });
        playSoundWithDelay(Sound.CORRECT_ANSWER);
        fadeInQuestionText(1500);
    }


    private void updateScoreView(Bundle bundle){
        viewModel.scoreValue = getInt(bundle, Tag.SCORE);
        scoreTextView.setText(createScoreString(viewModel.scoreValue));
    }


    private void changeInputTextColorForCorrectAnswer(int colorChangeDuration){
        int correctAnswerTextColor = getColorFromAttribute(R.attr.correct_answer_text_color, getContext());
        animateTextColor(inputTextView, defaultAnswerTextColor, correctAnswerTextColor, 0, colorChangeDuration);
    }


    private String createScoreString(int score){
        return String.valueOf(score);
    }


    private void setQuestion(Bundle bundle){
        boolean wasQuestionTextEmpty = viewModel.questionText.isEmpty();
        viewModel.questionText = getStr(bundle, Tag.QUESTION);
        if(questionTextView.getVisibility() != View.VISIBLE || wasQuestionTextEmpty){
            fadeInQuestionText(500);
        }

    }


    private void fadeOutQuestionText(){
        runOnUiThread(()-> questionTextView.startAnimation(textAnimator.getFadeOutAnimation()));
    }


    private void fadeInQuestionText(int delay){
        new Handler(Looper.getMainLooper()).postDelayed( ()-> {
            questionTextView.setText(viewModel.questionText);
            questionTextView.startAnimation(textAnimator.getFadeInAnimation());
        },delay);
    }


    private void onGameOver(Bundle bundle){
        notifyGameOverAndPlaySound();
        runOnUiThread(()->inputHelper.clearAnswerText());
        resetViewDataAndLoadGameOver(bundle);
    }


    private void notifyGameOverAndPlaySound(){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity != null) {
            mainActivity.notifyServiceThatGameHasFinished();
            mainActivity.playSound(Sound.GAME_OVER);
        }
    }


    private void resetViewDataAndLoadGameOver(Bundle bundle){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            FragmentUtils.loadFragment(this, new GameOverFragment(), GameOverFragment.FRAGMENT_TAG, bundle);
            resetViewData();
        }, 400);
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