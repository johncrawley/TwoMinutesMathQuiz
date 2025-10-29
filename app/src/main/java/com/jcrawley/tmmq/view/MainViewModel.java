package com.jcrawley.tmmq.view;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.jcrawley.tmmq.service.game.GameModel;

public class MainViewModel extends ViewModel {

    public GameModel gameModel = new GameModel();

    public String currentAnswerText = "";
    public int gameStartInitialCountdown = 3;
    public int gameStartCurrentCountdown = 3;
    public int startScreenVisibility = View.VISIBLE;
    public int gameScreenVisibility, gameOverScreenVisibility = View.GONE;
}
