package com.jcrawley.tmmq.service.score;

import com.jcrawley.tmmq.service.game.level.GameLevel;

public class ScoreStatistics {


    private GameLevel gameLevel;
    private int finalScore;
    private String timerLength;
    private int dailyHighScore;
    private int allTimeHighScore;


    public GameLevel getGameLevel(){
        return gameLevel;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getTimerLength() {
        return timerLength;
    }

    public void setTimerLength(String timerLength) {
        this.timerLength = timerLength;
    }

    public int getDailyHighScore() {
        return dailyHighScore;
    }

    public void setDailyHighScore(int dailyHighScore) {
        this.dailyHighScore = dailyHighScore;
    }

    public int getAllTimeHighScore() {
        return allTimeHighScore;
    }

    public void setAllTimeHighScore(int allTimeHighScore) {
        this.allTimeHighScore = allTimeHighScore;
    }


}
