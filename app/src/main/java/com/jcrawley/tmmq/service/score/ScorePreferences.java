package com.jcrawley.tmmq.service.score;

public interface ScorePreferences {

    void saveHighScore(int score, String timerLength, String difficulty);
    void saveDailyHighScore(int score, String timerLength, String difficulty, String currentDateStr);
    int getHighScore(String timerLength, String difficulty);
    int getDailyHighScore(String timerLength, String difficulty, String currentDateStr);
}
