package com.jcrawley.tmmq.view;

import android.view.View;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public String currentAnswerText = "";
    public int gameStartInitialCountdown = 3;
    public int gameStartCurrentCountdown = 3;
    public int startScreenVisibility = View.VISIBLE;
    public int gameScreenVisibility, gameOverScreenVisibility = View.GONE;
}
