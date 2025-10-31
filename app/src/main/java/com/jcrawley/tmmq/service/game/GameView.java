package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.game.question.MathQuestion;

public interface GameView {

    void resetScore();
    void setQuestion(MathQuestion question);
    void notifyIncorrectAnswer();
    void setScore(int score);
    void updateTimer(int minutesRemaining, int secondsRemaining);
    void loadGameOverScreen();
}
