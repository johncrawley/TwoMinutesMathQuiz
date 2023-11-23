package com.jcrawley.tmmq.service.score;

import android.content.SharedPreferences;

import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.level.GameLevel;

import java.time.LocalDateTime;



public class ScoreRecords {

    private enum RecordType { DAILY, ALL_TIME }
    private final String LAST_RECORD_DATE_KEY = "last_record_date";

    public final SharedPreferences scorePrefs;


    public ScoreRecords(SharedPreferences scorePreferences){
        this.scorePrefs = scorePreferences;
    }


    private void getHighScore(){

    }


    private String createScorePrefKey(Game game){
        GameLevel gameLevel = game.getCurrentLevel();
        int timerLength = game.getInitialTimer();
        return "scoreFor_" + gameLevel.toString() + "_"  + timerLength;
    }


    private boolean saveHighScore(ScoreStatistics scoreStatistics, int finalScore, RecordType recordType){
        String allTimeKey = createScorePrefKey(recordType, scoreStatistics);
        int allTimeScore = scorePrefs.getInt(allTimeKey, 0);
        if(finalScore > allTimeScore){
            scorePrefs.edit().putInt(allTimeKey, finalScore).apply();
            return true;
        }
        return false;
    }


    private int getAllTimeHighScoreRecord(ScoreStatistics scoreStatistics){
        String allTimeKey = createScorePrefKey(RecordType.ALL_TIME, scoreStatistics);
        return scorePrefs.getInt(allTimeKey, 0);
    }


    public ScoreStatistics getCompleteScoreStatsAndSaveRecords(ScoreStatistics stats){
        ScoreStatistics fullStats = new ScoreStatistics();

        int dailyRecord = getDailyHighScoreRecord(stats);
        int allTimeRecord = getAllTimeHighScoreRecord(stats);
        int finalScore = stats.getFinalScore();

        fullStats.setDailyHighScore(dailyRecord);
        fullStats.setAllTimeHighScore(allTimeRecord);
        fullStats.setFinalScore(finalScore);
        fullStats.setGameLevel(stats.getGameLevel());
        fullStats.setTimerLength(stats.getTimerLength());

        if(finalScore > dailyRecord){
            saveDailyHighScore(stats, finalScore);
        }
        if(finalScore > allTimeRecord){
            saveHighScore(stats, finalScore, RecordType.ALL_TIME);
        }
        return fullStats;
    }


    private void processDailyHighScore(ScoreStatistics scoreStatistics, int finalScore){
        int dailyRecord = getDailyHighScoreRecord(scoreStatistics);
        if(finalScore > dailyRecord){
            saveDailyHighScore(scoreStatistics, finalScore);
        }
    }


    private String getDateToday(){
        LocalDateTime dateToday = LocalDateTime.now();
        return  dateToday.getDayOfMonth()
                + "-" + dateToday.getMonthValue()
                + "-" + dateToday.getYear();
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


    private void saveDailyHighScore(ScoreStatistics scoreStatistics, int finalScore){
        String todayDateStr = getDateToday();
        String scoreKey = createScorePrefKey(RecordType.DAILY, scoreStatistics);
        saveDate(scorePrefs, todayDateStr);
        saveScore(scorePrefs, scoreKey, finalScore);
    }



    private String createScorePrefKey(RecordType recordType, ScoreStatistics scoreStatistics){
        return "scoreFor_" + recordType.toString()
                + "_" + scoreStatistics.getGameLevel().toString()
                + "_"  + scoreStatistics.getTimerLength();
    }


    private void saveDate(SharedPreferences sharedPreferences, String dateStr){
        sharedPreferences.edit().putString(LAST_RECORD_DATE_KEY, dateStr).apply();
    }


    private void saveScore(SharedPreferences sharedPreferences, String key, int score){
        sharedPreferences.edit().putInt(key, score).apply();
    }


}
