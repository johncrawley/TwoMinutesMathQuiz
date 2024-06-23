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
    public void saveDailyHighScore(int score, String timerLength, String difficulty, String dateStr) {
        save(RecordType.DAILY, score, timerLength, difficulty);
        saveDateFor(timerLength, difficulty, dateStr);
    }


    private void saveDateFor(String timerLength, String difficulty, String dateStr){
        sharedPreferences.edit().putString(createDateKeyFor(timerLength, difficulty), dateStr).apply();
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
    public int getDailyHighScore(String timerLength, String difficulty, String dateStr) {
        if(getMostRecentDateFor(timerLength, difficulty).equals(dateStr)){
            return get(RecordType.DAILY, timerLength, difficulty);
        }
        return 0;
    }


    private String getMostRecentDateFor(String timerLength, String difficulty){
        return sharedPreferences.getString(createDateKeyFor(timerLength,difficulty), "");
    }


    private String createDateKeyFor(String timerLength, String difficulty){
        return "date_" + timerLength + "__" + difficulty;
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


}
