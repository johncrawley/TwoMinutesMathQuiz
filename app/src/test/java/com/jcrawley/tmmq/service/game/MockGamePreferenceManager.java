package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.preferences.GamePreferenceManager;

public class MockGamePreferenceManager implements GamePreferenceManager {

    private int timer;
    private int timerIndex;
    private int level;

    @Override
    public void saveTimer(int value) {
        this.timer = value;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void saveTimerIndex(int value) {
        this.timerIndex = value;
    }

    @Override
    public int getTimerIndex() {
        return timerIndex;
    }

    @Override
    public void saveLevel(int value) {
        this.level = value;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
