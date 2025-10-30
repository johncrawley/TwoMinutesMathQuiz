package com.jcrawley.tmmq.view;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.jcrawley.tmmq.service.game.GameModel;
import com.jcrawley.tmmq.service.game.timer.GameTimerModel;

public class MainViewModel extends ViewModel {

    public GameModel gameModel = new GameModel();
    public GameTimerModel gameTimerModel = new GameTimerModel();

    public int gameStartInitialCountdown = 3;
    public int gameStartCurrentCountdown = 3;
}
