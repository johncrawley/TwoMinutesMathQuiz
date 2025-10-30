package com.jcrawley.tmmq.service.game.timer;


public class GameTimerModel {

    private int initialRemainingTime = 14;
    private int currentRemainingTime = initialRemainingTime;
    private int minutesRemaining, secondsRemaining;


    public void setInitialRemainingTime(int initialRemainingTime){
        this.initialRemainingTime = initialRemainingTime;
    }


    public int getMinutesRemaining(){
        return minutesRemaining;
    }


    public int getSecondsRemaining(){
        return secondsRemaining;
    }


    public void resetTime(){
        currentRemainingTime = initialRemainingTime;
        calculateMinutesAndSeconds();
    }


    void updateCurrentRemainingTime(){
        currentRemainingTime = Math.max(0, currentRemainingTime -1);
        calculateMinutesAndSeconds();
    }


    public boolean isTimeUp(){
        return currentRemainingTime <= 0;
    }


    public void calculateMinutesAndSeconds(){
        minutesRemaining = currentRemainingTime / 60;
        secondsRemaining = currentRemainingTime % 60;
    }

}
