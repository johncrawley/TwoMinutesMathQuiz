package com.jcrawley.tmmq.view;

import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;

public class InputHelper {

    private final MainActivity activity;
    private final MainViewModel viewModel;
    private final TextView inputTextView;

    public InputHelper(MainActivity activity){
        this.activity = activity;
        viewModel = activity.getViewModel();
        inputTextView = activity.findViewById(R.id.inputText);
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


    private void setupButton(int buttonId, Runnable runnable){
        Button button = activity.findViewById(buttonId);
        button.setOnClickListener(v->{
            runnable.run();
            activity.vibrateOnPress();
        });
    }


    private void submitAnswer(){
        activity.submitAnswer();
        clearAnswerText();
    }


    private void setupButtonForAdd(int buttonId, int digit){
        Button button = activity.findViewById(buttonId);
        button.setOnClickListener(v-> {
            addDigitToAnswer(digit);
            activity.vibrateOnPress();
        });
    }


    private void backspace(){
        if(viewModel.currentAnswerText.isEmpty()){
            return;
        }
        int amendedValue = getAnswerNumber() / 10;
        setAnswerText(amendedValue);
    }


    private void addDigitToAnswer(int digit){
        int maxNumberOfDigits = 5;
        if(viewModel.currentAnswerText.length() >= maxNumberOfDigits){
            return;
        }
        int amendedValue = (getAnswerNumber() * 10) + digit;
        setAnswerText(amendedValue);
    }


    private int getAnswerNumber(){
        if(viewModel.currentAnswerText.trim().isEmpty()){
            return 0;
        }
        return Integer.parseInt(viewModel.currentAnswerText);
    }


    private void setAnswerText(int number){
        viewModel.currentAnswerText = String.valueOf(number);
        inputTextView.setText(viewModel.currentAnswerText);
    }


    private void clearAnswerText(){
        viewModel.currentAnswerText = "";
        inputTextView.setText(viewModel.currentAnswerText);
    }

}
