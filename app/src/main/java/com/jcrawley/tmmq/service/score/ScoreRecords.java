package com.jcrawley.tmmq.service.score;


import java.time.LocalDateTime;


public class ScoreRecords {

    private ScorePreferences scorePreferences;
    private CurrentDateGenerator currentDateGenerator;


    public void setScorePreferences(ScorePreferences scorePreferences){
        this.scorePreferences = scorePreferences;
    }


    public void setCurrentDateCreator(CurrentDateGenerator currentDateGenerator){
        this.currentDateGenerator = currentDateGenerator;
    }


    public ScoreStatistics getCompleteScoreStatsAndSaveRecords(ScoreStatistics stats){
        String timerLength = stats.getTimerLength();
        String difficulty = stats.getGameLevel().getDifficultyStr();
        int finalScore = stats.getFinalScore();
        int oldDailyRecord = getDailyRecord(timerLength, difficulty);
        int oldHighScore = scorePreferences.getHighScore(timerLength, difficulty);

        saveDailyHighScore(finalScore, oldDailyRecord, timerLength, difficulty);
        saveAllTimeHighScore(finalScore, oldHighScore, timerLength, difficulty);
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


    private void saveAllTimeHighScore(int score, int highScore, String timerLength, String difficulty){
        if(score > highScore){
            scorePreferences.saveHighScore(score, timerLength, difficulty);
        }
    }


    private void saveDailyHighScore(int score, int highScore, String timerLength, String difficulty){
        if(score > highScore){
            scorePreferences.saveDailyHighScore(score, timerLength, difficulty, currentDateGenerator.get());
        }
    }


    private int getDailyRecord(String timerLength, String difficulty){
        return scorePreferences.getDailyHighScore(timerLength, difficulty, currentDateGenerator.get());
    }





}
