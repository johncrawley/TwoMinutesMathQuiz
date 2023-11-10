package com.jcrawley.tmmq.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.fragments.GameScreenViewModel;

public class InputHelper {

    private final MainActivity activity;
   // private final MainViewModel viewModel;
    private final TextView inputTextView;
    private final View parentView;
    private final GameScreenViewModel viewModel;

    public InputHelper(MainActivity activity, View parentView, GameScreenViewModel viewModel){
        this.activity = activity;
        this.parentView = parentView;
        this.viewModel = viewModel;
        //viewModel = activity.getViewModel();
        inputTextView = parentView.findViewById(R.id.inputText);
        setupButtons();
        clearAnswerText();
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


    private void setupButton(int buttonId, Runnable runnable){
        Button button = parentView.findViewById(buttonId);
        button.setOnClickListener(v->{
            runnable.run();
            activity.vibrateOnPress();
        });
    }


    private void submitAnswer(){
        String answer = viewModel.inputText.trim();
        if(answer.isEmpty()){
            return;
        }
        activity.submitAnswer(answer);
        clearAnswerText();
    }


    private void setupButtonForAdd(int buttonId, int digit){
        Button button = parentView.findViewById(buttonId);
        button.setOnClickListener(v-> {
            addDigitToAnswer(digit);
            activity.vibrateOnPress();
        });
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


    private void clearAnswerText(){
        viewModel.inputText = "";
        inputTextView.setText(viewModel.inputText);
    }

}
