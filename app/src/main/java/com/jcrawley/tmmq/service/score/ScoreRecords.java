package com.jcrawley.tmmq.service.score;

import android.content.SharedPreferences;

import java.time.LocalDateTime;


public class ScoreRecords {

    private enum RecordType { DAILY, ALL_TIME }
    private final String LAST_RECORD_DATE_KEY = "last_record_date";

    public final SharedPreferences scorePrefs;


    public ScoreRecords(SharedPreferences scorePreferences){
        this.scorePrefs = scorePreferences;
    }


    public ScoreStatistics getCompleteScoreStatsAndSaveRecords(ScoreStatistics stats){
        int existingDailyRecord = getDailyHighScoreRecord(stats);
        int existingAllTimeRecord = getAllTimeHighScoreRecord(stats);

        saveDailyHighScore(stats, stats.getFinalScore(), existingDailyRecord);
        saveAllTimeHighScore(stats, stats.getFinalScore(), existingAllTimeRecord);
        return buildFullStatsFrom(stats, existingDailyRecord, existingAllTimeRecord);
    }


    private int getAllTimeHighScoreRecord(ScoreStatistics scoreStatistics){
        String allTimeKey = createScorePrefKey(RecordType.ALL_TIME, scoreStatistics);
        return scorePrefs.getInt(allTimeKey, 0);
    }


    private ScoreStatistics buildFullStatsFrom(ScoreStatistics endGameStats, int currentDailyRecord, int currentAllTimeRecord){
        ScoreStatistics fullStats = new ScoreStatistics();
        fullStats.setDailyHighScore(currentDailyRecord);
        fullStats.setAllTimeHighScore(currentAllTimeRecord);
        fullStats.setFinalScore(endGameStats.getFinalScore());
        fullStats.setGameLevel(endGameStats.getGameLevel());
        fullStats.setTimerLength(endGameStats.getTimerLength());
        return fullStats;
    }


    private void saveAllTimeHighScore(ScoreStatistics scoreStatistics, int finalScore, int currentAllTimeRecord){
        if(finalScore <= currentAllTimeRecord){
            return;
        }
        String allTimeKey = createScorePrefKey(RecordType.ALL_TIME, scoreStatistics);
        scorePrefs.edit().putInt(allTimeKey, finalScore).apply();
    }


    private void saveDailyHighScore(ScoreStatistics scoreStatistics, int finalScore, int currentDailyRecord){
        if(finalScore <= currentDailyRecord){
            return;
        }
        String todayDateStr = getDateToday();
        String scoreKey = createScorePrefKey(RecordType.DAILY, scoreStatistics);
        saveDate(scorePrefs, todayDateStr);
        saveScore(scorePrefs, scoreKey, finalScore);
    }


    private int getDailyHighScoreRecord(ScoreStatistics scoreStatistics){
        String todayDateStr = getDateToday();
        String lastDateStr = scorePrefs.getString(LAST_RECORD_DATE_KEY, todayDateStr);
        if(!lastDateStr.equals(todayDateStr)){
            return 0;
        }
        String scoreKey = createScorePrefKey(RecordType.DAILY, scoreStatistics);
        return scorePrefs.getInt(scoreKey, 0);
    }


    private String getDateToday(){
        LocalDateTime dateToday = LocalDateTime.now();
        return  dateToday.getDayOfMonth()
                + "-" + dateToday.getMonthValue()
                + "-" + dateToday.getYear();
    }


    private String createScorePrefKey(RecordType recordType, ScoreStatistics scoreStatistics){
        return "scoreFor_" + recordType.toString()
                + "_" + scoreStatistics.getGameLevel().getDifficultyStr()
                + "_"  + scoreStatistics.getTimerLength();
    }


    private void saveDate(SharedPreferences sharedPreferences, String dateStr){
        sharedPreferences.edit().putString(LAST_RECORD_DATE_KEY, dateStr).apply();
    }


    private void saveScore(SharedPreferences sharedPreferences, String key, int score){
        sharedPreferences.edit().putInt(key, score).apply();
    }


}
