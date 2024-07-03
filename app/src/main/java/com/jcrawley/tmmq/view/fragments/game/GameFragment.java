package com.jcrawley.tmmq.view.fragments.game;

import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.NOTIFY_GAME_OVER;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.NOTIFY_INCORRECT_ANSWER;
import static com.jcrawley.tmmq.view.fragments.game.GameFragment.Message.SET_TIME_REMAINING;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.animateTextColor;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.getBoolean;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.getInt;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.getStr;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.TextAnimator;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;
import com.jcrawley.tmmq.view.fragments.GameOverFragment;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class GameFragment extends Fragment {

    public enum Message {SET_TIME_REMAINING, NOTIFY_GAME_OVER, NOTIFY_INCORRECT_ANSWER, SET_SCORE, SET_QUESTION  }
    public enum Tag { MINUTES_REMAINING, SECONDS_REMAINING, SCORE, QUESTION, IS_QUESTION_USING_AN_EXPONENT}
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
        setupBackButton();
        isCreated.set(true);
        return parentView;
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


    private void setupBackButton(){
        FragmentUtils.onBackButtonPressed(this, () ->{
            resetViewData();
            stopTimerAndReturnToWelcomeScreen();
        });
    }


    private void process(Consumer<Bundle> consumer, Bundle bundle){
        if(isCreated.get()){
            consumer.accept(bundle);
        }
    }


    private void setupViews(View parentView){
        setupStatLabels(parentView);
        timeRemainingTextView = setupTextView(parentView.findViewById(R.id.remaining_time_stat_include), R.id.statValue, viewModel.timeRemaining);
        scoreTextView = setupTextView(parentView.findViewById(R.id.score_stat_include), R.id.statValue, createScoreString(viewModel.scoreValue));
        updateScoreTextView();
        questionTextView = setupTextView(parentView, R.id.questionText, viewModel.questionText);
        inputTextView = setupTextView(parentView, R.id.inputText, viewModel.inputText);
        textAnimator = new TextAnimator(questionTextView);
    }


    private void setupStatLabels(View parentView){
        setupLabel(parentView, R.id.score_stat_include, R.string.score_label_text);
        setupLabel(parentView, R.id.remaining_time_stat_include, R.string.time_remaining_label);
    }


    private void setupLabel(View parentView, int labelLayout, int strId){
        ViewGroup layout = parentView.findViewById(labelLayout);
        if(layout != null){
            TextView label = layout.findViewById(R.id.statLabel);
            label.setText(getString(strId));
        }
    }


    private TextView setupTextView(View parentView, int id, String viewModelValue){
        TextView textView = parentView.findViewById(id);
        textView.setText(viewModelValue);
        return textView;
    }


    private TextView setupTextView(View parentView, int id, Spanned viewModelValue){
        TextView textView = parentView.findViewById(id);
        textView.setText(viewModelValue);
        return textView;
    }


    private void playSound(Sound sound){
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
        Activity activity = getActivity();
        if(activity == null){
            return;
        }
        int totalSecondsRemaining = (minutesRemaining * 60) + secondsRemaining;
        int warningTime = activity.getResources().getInteger(R.integer.countdown_warning_time);
        timeRemainingTextView.setVisibility(View.VISIBLE);
        setTimeRemainingTextColor(totalSecondsRemaining, warningTime);
        handleWarningOnLowTime(totalSecondsRemaining, warningTime);
        viewModel.timeRemaining = createTimeRemainingString(minutesRemaining, secondsRemaining);
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


    private void onIncorrectAnswer(Bundle bundle){
        int incorrectColor = getColorFromAttribute(R.attr.incorrect_answer_text_color, getContext());
        int initialDelay = 0;
        int colorChangeDuration = 150;
        animateTextColor(inputTextView, defaultAnswerTextColor, incorrectColor, initialDelay, colorChangeDuration);
        clearInputTextAfterDelay(initialDelay + colorChangeDuration + 300);
        animateQuestionTextChange(colorChangeDuration + 600);
        playSound(Sound.INCORRECT_ANSWER);
    }


    private void setScore(Bundle bundle){
        runOnUiThread(()-> {
            updateScoreView(bundle);
            int colorChangeDuration = 150;
            changeInputTextColorForCorrectAnswer(colorChangeDuration);
            animateScoreOnUpdate();
            clearInputTextAfterDelay(colorChangeDuration + 100);
            animateQuestionTextChange(colorChangeDuration + 100);
        });
        playSound(Sound.CORRECT_ANSWER);
    }


    private void animateQuestionTextChange(int delay){
        new Handler(Looper.getMainLooper())
                .postDelayed(()-> textAnimator.animateTextChange(viewModel.questionText)
                        , delay);
    }


    private void animateScoreOnUpdate(){
        int normalColor = getColorFromAttribute(R.attr.score_normal_color, getContext());
        int flashColor = getColorFromAttribute(R.attr.score_flash_color, getContext());
        animateTextColor(scoreTextView, normalColor, flashColor, 10, 80);
        animateTextColor(scoreTextView, flashColor, normalColor, 200, 150);
    }


    public void clearInputTextAfterDelay(int delay){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            viewModel.inputText = "";
            inputTextView.setText(viewModel.inputText);
            inputTextView.setTextColor(defaultAnswerTextColor);
        },delay);
    }


    private void updateScoreView(Bundle bundle){
        viewModel.scoreValue = getInt(bundle, Tag.SCORE);
        updateScoreTextView();
    }


    private void changeInputTextColorForCorrectAnswer(int colorChangeDuration){
        int correctAnswerTextColor = getColorFromAttribute(R.attr.correct_answer_text_color, getContext());
        animateTextColor(inputTextView, defaultAnswerTextColor, correctAnswerTextColor, 0, colorChangeDuration);
    }


    private void updateScoreTextView(){
        scoreTextView.setText(String.valueOf(viewModel.scoreValue));
    }


    private String createScoreString(int score){
        return String.valueOf(score);
    }


    private void setQuestion(Bundle bundle){
        boolean wasExistingQuestionTextEmpty = viewModel.questionText.length() == 0;
        setQuestionText(bundle);
        turnExponentToSuperScript(bundle);
        fadeInFirstQuestionIf(wasExistingQuestionTextEmpty);
    }


    private void setQuestionText(Bundle bundle){
        String text = getStr(bundle, Tag.QUESTION);
        viewModel.questionText = new SpannableString(text);
    }


    private void turnExponentToSuperScript(Bundle bundle){
        if(getBoolean(bundle, Tag.IS_QUESTION_USING_AN_EXPONENT)){
            String text = getStr(bundle, Tag.QUESTION);
            int firstIndex = text.length()-2;
            viewModel.questionText.setSpan(new SuperscriptSpan(), firstIndex, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            viewModel.questionText.setSpan(new RelativeSizeSpan(0.5f), firstIndex, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }


    private void fadeInFirstQuestionIf(boolean wasExistingQuestionTextEmpty){
        if(questionTextView.getVisibility() != View.VISIBLE || wasExistingQuestionTextEmpty){
            fadeInQuestionText();
        }
    }


    private void fadeInQuestionText(){
        Activity activity = getActivity();
        if(activity == null){
            questionTextView.setText(viewModel.questionText);
            questionTextView.setVisibility(View.VISIBLE);
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed( ()-> {
            questionTextView.setText(viewModel.questionText);
            questionTextView.startAnimation(textAnimator.getFadeInAnimation());
        }, activity.getResources().getInteger(R.integer.question_text_fade_in));
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
        FragmentUtils.loadFragment(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }


    private void resetViewData(){
        viewModel.scoreValue = 0;
        viewModel.inputText = "";
        viewModel.timeRemaining = "";
        viewModel.questionText = new SpannableString("");
    }


    private void setupColors(){
        timeRemainingTextNormalColor = getColorFromAttribute(R.attr.time_remaining_text_normal_color, getContext());
        timeRemainingTextWarningColor = getColorFromAttribute(R.attr.time_remaining_text_warning_color, getContext());
        defaultAnswerTextColor = getColorFromAttribute(R.attr.default_answer_text_color, getContext());
    }


}