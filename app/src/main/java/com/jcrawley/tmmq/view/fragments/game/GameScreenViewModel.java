package com.jcrawley.tmmq.view.fragments.game;

import android.text.SpannableString;

import androidx.lifecycle.ViewModel;

public class GameScreenViewModel extends ViewModel {

    public String inputText = "";
    public String score = "0";
    public int scoreValue = 0;
    public String timeRemaining = "";
    public SpannableString questionText = new SpannableString("");
    public boolean isQuestionUsingAnExponent;
}
