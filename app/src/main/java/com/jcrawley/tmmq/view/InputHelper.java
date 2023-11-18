package com.jcrawley.tmmq.view;

import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.fragments.GameScreenViewModel;

public class InputHelper {

    private final MainActivity activity;
    private final TextView inputTextView;
    private final View parentView;
    private final GameScreenViewModel viewModel;

    public InputHelper(MainActivity activity, View parentView, GameScreenViewModel viewModel){
        this.activity = activity;
        this.parentView = parentView;
        this.viewModel = viewModel;
        inputTextView = parentView.findViewById(R.id.inputText);
        inputTextView.setText(viewModel.inputText);
        setupButtons();
    }


    private void setupButtons(){
        setupButtonForAdd(R.id.button0, 0);
        setupButtonForAdd(R.id.button1, 1);
        setupButtonForAdd(R.id.button2, 2);
        setupButtonForAdd(R.id.button3, 3);
        setupButtonForAdd(R.id.button4, 4);
        setupButtonForAdd(R.id.button5, 5);
        setupButtonForAdd(R.id.button6, 6);
        setupButtonForAdd(R.id.button7, 7);
        setupButtonForAdd(R.id.button8, 8);
        setupButtonForAdd(R.id.button9, 9);
        setupButton(R.id.buttonBackspace, this::backspace);
        setupButton(R.id.buttonEnter, this::submitAnswer);
    }


    private void setupButton(int viewId, Runnable runnable){
        View view = parentView.findViewById(viewId);
        setViewBrightnessOnClick(view, runnable);
    }


    private void setupButtonForAdd(int viewId, int digit){
        View view = parentView.findViewById(viewId);
        setViewBrightnessOnClick(view, ()-> addDigitToAnswer(digit));
    }



    @SuppressLint("ClickableViewAccessibility")
    private void setViewBrightnessOnClick(View view, Runnable onRelease){
        view.setOnTouchListener((view1, motionEvent) -> {
            int action = motionEvent.getAction();
            if(action == MotionEvent.ACTION_DOWN){
               // animateViewBrightness(view, R.attr.input_view_normal_color, R.attr.input_view_pressed_color);
                activity.vibrateOnPress();
            }
            else if(action == MotionEvent.ACTION_UP){
               // animateViewBrightness(view, R.attr.input_view_pressed_color, R.attr.input_view_normal_color);
                onRelease.run();
            }
            return true;
        });
    }


    private void animateViewBrightness(View view, int startColorAttributeId, int endColorAttributeId){
        int colorFrom = getColorFromAttribute(startColorAttributeId, activity);
        int colorTo = getColorFromAttribute(endColorAttributeId, activity);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(300);
        colorAnimation.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }


    private void submitAnswer(){
        String answer = viewModel.inputText.trim();
        if(answer.isEmpty()){
            return;
        }
        if(activity == null){
            return;
        }
        activity.submitAnswer(answer);
        clearAnswerText();
    }


    private void backspace(){
        if(viewModel.inputText.isEmpty()){
            return;
        }
        int amendedValue = getAnswerNumber() / 10;
        setAnswerText(amendedValue);
    }


    private void addDigitToAnswer(int digit){
        int maxNumberOfDigits = 5;
        if(viewModel.inputText.length() >= maxNumberOfDigits){
            return;
        }
        int amendedValue = (getAnswerNumber() * 10) + digit;
        setAnswerText(amendedValue);
    }


    private int getAnswerNumber(){
        if(viewModel.inputText.trim().isEmpty()){
            return 0;
        }
        return Integer.parseInt(viewModel.inputText);
    }


    private void setAnswerText(int number){
        viewModel.inputText = String.valueOf(number);
        inputTextView.setText(viewModel.inputText);
    }


    public void clearAnswerText(){
        viewModel.inputText = "";
        inputTextView.setText(viewModel.inputText);
    }

}
