package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

public interface GameView {

    void resetScore();
    void setQuestion(MathQuestion question);
    void notifyIncorrectAnswer();
    void setScore(int score);
    void gameOver(ScoreStatistics scoreStatistics);
    void updateTimer(int minutesRemaining, int secondsRemaining);
}
