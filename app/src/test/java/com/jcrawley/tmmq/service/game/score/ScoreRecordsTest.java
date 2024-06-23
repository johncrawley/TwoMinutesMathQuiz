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
    private ScoreStatistics stats;
    private CustomCurrentDateGenerator currentDateGenerator;
    private final int initialDifficulty = 1;
    private final String initialTimerLength = "1:00";

    @Before
    public void init(){
        scoreRecords = new ScoreRecords();
        scorePreferences = new CustomScorePreferences();
        currentDateGenerator = new CustomCurrentDateGenerator();
        scoreRecords.setScorePreferences(scorePreferences);
        scoreRecords.setCurrentDateCreator(currentDateGenerator);

        stats = new ScoreStatistics();
        stats.setGameLevel(new GameLevel(initialDifficulty));
        stats.setTimerLength("1:00");
    }


    @Test
    public void statisticsCanRecordANewHighScore(){
        int finalScore = 10;
        stats.setFinalScore(finalScore);
        var updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(finalScore, scorePreferences.getHighScore(initialTimerLength, String.valueOf(initialDifficulty)));
        assertEquals(finalScore, updatedScoreStats.getFinalScore());
        assertEquals(0, updatedScoreStats.getExistingHighScore());
        assertEquals(0, updatedScoreStats.getExistingDailyHighScore());
    }


    @Test
    public void highScoresAreRecalled(){
        int highScore = 10;
        stats.setFinalScore(highScore);
        scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);

        int lowerScore = 5;
        stats.setFinalScore(lowerScore);
        var updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(highScore, scorePreferences.getHighScore("",""));
        assertEquals(lowerScore, updatedScoreStats.getFinalScore());
        assertEquals(highScore, updatedScoreStats.getExistingHighScore());
        assertEquals(highScore, updatedScoreStats.getExistingDailyHighScore());

        int higherScore = 15;
        stats.setFinalScore(higherScore);
        updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(higherScore, scorePreferences.getHighScore("",""));
        assertEquals(higherScore, updatedScoreStats.getFinalScore());
        assertEquals(highScore, updatedScoreStats.getExistingHighScore());
        assertEquals(highScore, updatedScoreStats.getExistingDailyHighScore());


        int someOtherScore = 4;
        stats.setFinalScore(someOtherScore);
        updatedScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(someOtherScore, updatedScoreStats.getFinalScore());
        assertEquals(higherScore, updatedScoreStats.getExistingHighScore());
        assertEquals(higherScore, updatedScoreStats.getExistingDailyHighScore());
    }


    @Test
    public void dailyHighScoreIsRecalled(){
        int allTimeHighScore = 200;
        int todayScore1= 10;
        scorePreferences.saveHighScore(allTimeHighScore, "", "");

        stats.setFinalScore(todayScore1);
        var stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(this.stats);
        assertEquals(todayScore1, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(0, stats.getExistingDailyHighScore());


        int todayScore2 = 15;
        this.stats.setFinalScore(todayScore2);
        stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(this.stats);
        assertEquals(todayScore2, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(todayScore1, stats.getExistingDailyHighScore());


        int todayScore3 = 6;
        this.stats.setFinalScore(todayScore3);
        stats = scoreRecords.getCompleteScoreStatsAndSaveRecords(this.stats);
        assertEquals(todayScore3, stats.getFinalScore());
        assertEquals(allTimeHighScore, stats.getExistingHighScore());
        assertEquals(todayScore2, stats.getExistingDailyHighScore());
    }


    @Test
    public void highScoreForTodayIsNotAffectedByPreviousDay(){
        currentDateGenerator.setCurrentDate("day1");
        int score1 = 10;
        stats.setFinalScore(score1);
        var results = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(0, results.getExistingDailyHighScore());

        stats.setFinalScore(5);
        var results2 = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(score1, results2.getExistingDailyHighScore());

        currentDateGenerator.setCurrentDate("day2");
        int day2Score = 8;
        stats.setFinalScore(day2Score);
        assertResults(day2Score, 0, score1);

        int day2Score2 = 4;
        stats.setFinalScore(day2Score2);
        assertResults(day2Score2, day2Score, score1);
    }


    @Test
    public void highScoreForTodayIsNotAffectedByAnotherLevel(){
        GameLevel level1 = new GameLevel(initialDifficulty);
        GameLevel level2 = new GameLevel(initialDifficulty + 1);
        currentDateGenerator.setCurrentDate("day1");
        int score1 = 10;
        stats.setFinalScore(score1);
        scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);

        int score2 = 5;
        stats.setFinalScore(score2);
        stats.setGameLevel(level2);
        assertResults(score2, 0, 0);

        currentDateGenerator.setCurrentDate("day2");
        int score3 = 5;
        stats.setGameLevel(level1);
        stats.setFinalScore(score3);
        assertResults(score3, 0, score1);

        int score4 = 3;
        stats.setGameLevel(level2);
        stats.setFinalScore(score4);
        assertResults(score4, 0, score2);

        int score5 = 4;
        stats.setGameLevel(level2);
        stats.setFinalScore(score5);
        assertResults(score5, score4, score2);
    }


    private void assertResults(int finalScore, int existingHighScoreForToday, int existingHighScore){
        var results = scoreRecords.getCompleteScoreStatsAndSaveRecords(stats);
        assertEquals(finalScore, results.getFinalScore());
        assertEquals(existingHighScoreForToday, results.getExistingDailyHighScore());
        assertEquals(existingHighScore, results.getExistingHighScore());
    }


}
