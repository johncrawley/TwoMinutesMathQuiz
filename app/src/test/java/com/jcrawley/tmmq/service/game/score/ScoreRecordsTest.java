package com.jcrawley.tmmq.service.game.score;

import static org.junit.Assert.assertEquals;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.score.ScorePreferences;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

import org.junit.Before;
import org.junit.Test;

public class ScoreRecordsTest {

    private ScoreRecords scoreRecords;
    private ScorePreferences scorePreferences;
    private ScoreStatistics scoreStatistics;

    @Before
    public void init(){
        scoreRecords = new ScoreRecords();
        scorePreferences = new CustomScorePreferences();
        scoreRecords.setScorePreferences(scorePreferences);

        scoreStatistics = new ScoreStatistics();
        scoreStatistics.setGameLevel(new GameLevel(1));
        scoreStatistics.setTimerLength("1:00");
    }

    @Test
    public void statisticsCanRecordANewHighScore(){
        int finalScore = 10;
        scoreStatistics.setFinalScore(finalScore);
        var updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(finalScore, scorePreferences.getHighScore("",""));
        assertEquals(finalScore, updatedScoreStats.getFinalScore());
        assertEquals(0, updatedScoreStats.getExistingHighScore());
        assertEquals(0, updatedScoreStats.getExistingDailyHighScore());
    }


    @Test
    public void highScoresAreRecalled(){
        int highScore = 10;
        scoreStatistics.setFinalScore(highScore);
        scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);

        int lowerScore = 5;
        scoreStatistics.setFinalScore(lowerScore);
        var updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(highScore, scorePreferences.getHighScore("",""));
        assertEquals(lowerScore, updatedScoreStats.getFinalScore());
        assertEquals(highScore, updatedScoreStats.getExistingHighScore());
        assertEquals(highScore, updatedScoreStats.getExistingDailyHighScore());

        int higherScore = 15;
        scoreStatistics.setFinalScore(higherScore);
        updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(higherScore, scorePreferences.getHighScore("",""));
        assertEquals(higherScore, updatedScoreStats.getFinalScore());
        assertEquals(highScore, updatedScoreStats.getExistingHighScore());
        assertEquals(highScore, updatedScoreStats.getExistingDailyHighScore());


        int someOtherScore = 4;
        scoreStatistics.setFinalScore(someOtherScore);
        updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(someOtherScore, updatedScoreStats.getFinalScore());
        assertEquals(higherScore, updatedScoreStats.getExistingHighScore());
        assertEquals(higherScore, updatedScoreStats.getExistingDailyHighScore());
    }


    @Test
    public void dailyHighScoreIsRecalled(){
        int allTimeHighScore = 200;
        int todayScore1= 10;
        scorePreferences.saveHighScore(allTimeHighScore, "", "");

        scoreStatistics.setFinalScore(todayScore1);
        var stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(todayScore1, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(0, stats.getExistingDailyHighScore());


        int todayScore2 = 15;
        scoreStatistics.setFinalScore(todayScore2);
        stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(todayScore2, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(todayScore1, stats.getExistingDailyHighScore());


        int todayScore3 = 6;
        scoreStatistics.setFinalScore(todayScore3);
        stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        assertEquals(todayScore3, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(todayScore2, stats.getExistingDailyHighScore());
    }

}
