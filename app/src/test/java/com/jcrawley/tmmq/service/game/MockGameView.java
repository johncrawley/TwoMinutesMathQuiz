package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

public class MockGameView implements GameView{
    @Override
    public void resetScore() {

    }


    @Override
    public void setQuestion(MathQuestion question) {

    }


    @Override
    public void notifyIncorrectAnswer() {

    }


    @Override
    public void setScore(int score) {

    }


    @Override
    public void onGameOver(ScoreStatistics scoreStatistics) {

    }


    @Override
    public void updateTimer(int minutesRemaining, int secondsRemaining) {

    }

}
