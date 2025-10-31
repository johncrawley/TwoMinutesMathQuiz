package com.jcrawley.tmmq.service.score;


import com.jcrawley.tmmq.service.score.date.CurrentDateGenerator;
import com.jcrawley.tmmq.service.score.preferences.ScorePreferences;

public class ScoreRecords {

    private final ScorePreferences scorePreferences;
    private final CurrentDateGenerator currentDateGenerator;


    public ScoreRecords(ScorePreferences scorePreferences, CurrentDateGenerator currentDateGenerator){
        this.scorePreferences = scorePreferences;
        this.currentDateGenerator = currentDateGenerator;
    }


    public ScoreStatistics getCompleteScoreStats(ScoreStatistics stats){
        String timerLength = stats.getTimerLength();
        String difficulty = stats.getGameLevel().getDifficultyStr();
        int oldDailyRecord = getDailyRecord(timerLength, difficulty);
        int oldHighScore = scorePreferences.getHighScore(timerLength, difficulty);

        return buildFullStatsFrom(stats, oldDailyRecord, oldHighScore);
    }


    private ScoreStatistics buildFullStatsFrom(ScoreStatistics endGameStats, int existingDailyRecord, int existingHighScore){
        var fullStats = new ScoreStatistics();
        fullStats.setDailyHighScore(existingDailyRecord);
        fullStats.setAllTimeHighScore(existingHighScore);
        fullStats.setFinalScore(endGameStats.getFinalScore());
        fullStats.setGameLevel(endGameStats.getGameLevel());
        fullStats.setTimerLength(endGameStats.getTimerLength());
        return fullStats;
    }


    private int getDailyRecord(String timerLength, String difficulty){
        return scorePreferences.getDailyHighScore(timerLength, difficulty, currentDateGenerator.get());
    }


}
