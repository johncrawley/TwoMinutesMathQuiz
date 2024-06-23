package com.jcrawley.tmmq.service.game.score;

import com.jcrawley.tmmq.service.score.ScorePreferences;

import java.util.HashMap;
import java.util.Map;

public class CustomScorePreferences implements ScorePreferences {

    private final Map<String, Integer> highScoreMap;
    private final Map<String, Integer> todayHighScoreMap;
    private final Map<String, String> dateMap;


    public CustomScorePreferences(){
        highScoreMap = new HashMap<>();
        todayHighScoreMap = new HashMap<>();
        dateMap = new HashMap<>();
    }


    @Override
    public void saveHighScore(int score, String timerLength, String difficulty) {
        addScoreToMap(highScoreMap, score, timerLength, difficulty);
    }


    @Override
    public void saveDailyHighScore(int score, String timerLength, String difficulty, String currentDateStr) {
        addScoreToMap(todayHighScoreMap, score, timerLength, difficulty);
        saveDate(currentDateStr, timerLength, difficulty);
    }


    private String generateMapKey(String timerLength, String difficulty){
        return timerLength + "--" + difficulty;
    }

    private void addScoreToMap(Map<String, Integer> map, int score, String timerLength, String difficulty){
        map.put(generateMapKey(timerLength, difficulty), score);
    }


    @Override
    public int getHighScore(String timerLength, String difficulty) {
        return getScoreFromMap(highScoreMap, timerLength, difficulty);
    }


    @Override
    public int getDailyHighScore(String timerLength, String difficulty, String date) {
        String savedDate = getSavedDate(timerLength, difficulty);
        log("getDailyHighScore(), date: " + date + " savedDate: " + savedDate);
        if(savedDate.equals(date)){
            return getScoreFromMap(todayHighScoreMap, timerLength, difficulty);
        }
        return 0;
    }


    private void log(String msg){
        System.out.println("^^^ TEST CustomScorePreferences:  " + msg);
    }


    private int getScoreFromMap(Map<String, Integer> map, String timerLength, String difficulty){
        Integer result = map.getOrDefault(generateMapKey(timerLength, difficulty), 0);
        log("getScoreFromMap() result: " + result);
        return result == null ? 0 : result;
    }


    public void saveDate(String dateStr, String timerLength, String difficulty) {
        dateMap.put(generateMapKey(timerLength, difficulty), dateStr);
    }


    private String getSavedDate(String timerLength, String difficulty) {
        String result = dateMap.get(generateMapKey(timerLength, difficulty));
        return result == null ? "" : result;
    }
}
