package com.jcrawley.tmmq.service.score;

import android.content.SharedPreferences;


public class ScorePreferencesImpl implements ScorePreferences{

    private final SharedPreferences sharedPreferences;
    public enum RecordType { DAILY, ALL_TIME }
    private final String LAST_RECORD_DATE_KEY = "last_record_date";


    public ScorePreferencesImpl(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }


    @Override
    public void saveHighScore(int score, String timerLength, String difficulty) {
        save(RecordType.ALL_TIME, score, timerLength, difficulty);
    }


    @Override
    public void saveDailyHighScore(int score, String timerLength, String difficulty) {
        save(RecordType.DAILY, score, timerLength, difficulty);
    }


    private void save(RecordType recordType, int score, String timerLength, String difficulty){
        String key = createScorePrefKey(recordType, timerLength, difficulty);
        sharedPreferences.edit().putInt(key, score).apply();
    }


    @Override
    public int getHighScore(String timerLength, String difficulty) {
        return get(RecordType.ALL_TIME, timerLength, difficulty);
    }


    @Override
    public int getDailyHighScore(String timerLength, String difficulty) {
        return get(RecordType.DAILY, timerLength, difficulty);
    }


    public int get(RecordType recordType, String timerLength, String difficulty){
        String key = createScorePrefKey(recordType, timerLength, difficulty);
        return sharedPreferences.getInt(key, 0);
    }


    private String createScorePrefKey(RecordType recordType, String timerLength, String difficulty){
        return "scoreFor_" + recordType.toString()
                + "_" + difficulty
                + "_"  + timerLength;
    }


    @Override
    public void saveDate(String dateStr){
        sharedPreferences.edit().putString(LAST_RECORD_DATE_KEY,  dateStr).apply();
    }


    @Override
    public String getSavedDate(){
        return sharedPreferences.getString(LAST_RECORD_DATE_KEY, "");
    }


}
