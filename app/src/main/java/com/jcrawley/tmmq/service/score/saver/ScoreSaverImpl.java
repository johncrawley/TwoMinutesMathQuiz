package com.jcrawley.tmmq.service.score.saver;

import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.service.score.date.CurrentDateGenerator;
import com.jcrawley.tmmq.service.score.preferences.ScorePreferences;

public class ScoreSaverImpl implements ScoreSaver {


    private final ScorePreferences scorePreferences;
    private final CurrentDateGenerator currentDateGenerator;

    public ScoreSaverImpl(ScorePreferences scorePreferences, CurrentDateGenerator currentDateGenerator){
        this.scorePreferences = scorePreferences;
        this.currentDateGenerator = currentDateGenerator;
    }

    @Override
    public void saveScores(ScoreStatistics stats){
        int score = stats.getFinalScore();
        var timer = stats.getTimerLength();
        var difficulty = stats.getGameLevel().getDifficultyStr();
        int highScore = scorePreferences.getHighScore(timer, difficulty);
        saveAllTimeHighScore(score, highScore, timer, difficulty);
        saveDailyHighScore(score, highScore, timer, difficulty);
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

}
