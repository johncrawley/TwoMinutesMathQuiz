package com.jcrawley.tmmq.service.game.score;

import com.jcrawley.tmmq.service.score.ScorePreferences;

public class CustomScorePreferences implements ScorePreferences {

    private int highScore, dailyHighScore;
    private String dateStr = "";

    @Override
    public void saveHighScore(int score, String timerLength, String difficulty) {
        highScore = score;
    }


    @Override
    public void saveDailyHighScore(int score, String timerLength, String difficulty) {
        dailyHighScore = score;
    }


    @Override
    public int getHighScore(String timerLength, String difficulty) {
        return highScore;
    }


    @Override
    public int getDailyHighScore(String timerLength, String difficulty) {
        return dailyHighScore;
    }


    @Override
    public void saveDate(String dateStr) {
        this.dateStr = dateStr;
    }


    @Override
    public String getSavedDate() {
        return dateStr;
    }
}
