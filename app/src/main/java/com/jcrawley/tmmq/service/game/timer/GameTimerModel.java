package com.jcrawley.tmmq.service.game.timer;


import java.util.concurrent.Future;

public class GameTimerModel {

    private int initialRemainingTime = 14;
    private int currentRemainingTime = initialRemainingTime;
    private int minutesRemaining, secondsRemaining;
    private boolean wasStarted;
    private Future<?> future;


    public void start(){
        cancelFuture();
        wasStarted = true;
    }


    public void saveFuture(Future<?> future){
        this.future = future;
    }


    private void cancelFuture(){
        if(future != null && !future.isCancelled() && !future.isDone()){
            future.cancel(false);
        }
    }


    public void stop(){
        wasStarted = false;
    }


    public boolean shouldTimerBeRunning(){
        return wasStarted;
    }


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
