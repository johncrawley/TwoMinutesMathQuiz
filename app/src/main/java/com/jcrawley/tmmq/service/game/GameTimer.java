package com.jcrawley.tmmq.service.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameTimer {

    private final Game game;
    private final int initialRemainingTime = 14;
    private int currentRemainingTime = initialRemainingTime;
    private int minutesRemaining, secondsRemaining;
    private ScheduledFuture<?> future;
    private final ScheduledExecutorService scheduledExecutorService;

    public GameTimer(Game game){
        this.game = game;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        calculateMinutesAndSeconds();
    }

    public void startTimer(){
        future = scheduledExecutorService.scheduleAtFixedRate(this::decrementRemainingTime, 1,1, TimeUnit.SECONDS);
    }


    public void resetTime(){
        currentRemainingTime = initialRemainingTime;
        calculateMinutesAndSeconds();
    }


    private void decrementRemainingTime(){
        updateCurrentRemainingTime();
        updateTimer();
        cancelTimerWhenTimeExpires();
    }


    public int getSecondsRemaining(){
        return secondsRemaining;
    }


    public int getMinutesRemaining(){
        return minutesRemaining;
    }


    private void updateCurrentRemainingTime(){
        currentRemainingTime = Math.max(0, currentRemainingTime -1);
        calculateMinutesAndSeconds();
    }


    private void calculateMinutesAndSeconds(){
        minutesRemaining = currentRemainingTime / 60;
        secondsRemaining = currentRemainingTime % 60;
    }


    private void updateTimer(){
        game.updateTime(minutesRemaining, secondsRemaining);
    }



    private void cancelTimerWhenTimeExpires(){
        if(currentRemainingTime <= 0){
            future.cancel(false);
            game.gameOver();
            resetTime();
        }
    }

}
