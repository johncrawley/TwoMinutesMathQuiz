package com.jcrawley.tmmq;

public class GameTimer {

    private final Game game;
    private final int initialRemainingTime = 120;
    private int currentRemainingTime = initialRemainingTime;
    private int minutesRemaining, secondsRemaining;

    public GameTimer(Game game){
        this.game = game;
    }


    private void updateTimer(){
        game.updateTime(minutesRemaining, secondsRemaining);
    }


    public void resetTime(){
        currentRemainingTime = initialRemainingTime;
    }


    private void decrementRemainingTime(){
        currentRemainingTime--;
        updateCurrentRemainingTime();
        updateTimer();
        if(currentRemainingTime <= 0){
            stopTimer();
        }
    }

    private void updateCurrentRemainingTime(){
        minutesRemaining = currentRemainingTime / 60;
        secondsRemaining = currentRemainingTime % 60;
    }


    private void stopTimer(){

    }

}
