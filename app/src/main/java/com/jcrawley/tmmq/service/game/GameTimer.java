package com.jcrawley.tmmq.service.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameTimer {

    private final Game game;
    private final int initialRemainingTime = 120;
    private int currentRemainingTime = initialRemainingTime;
    private int minutesRemaining, secondsRemaining;
    private ScheduledFuture<?> future;
    private final ScheduledExecutorService scheduledExecutorService;

    public GameTimer(Game game){
        this.game = game;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void startTimer(){
        future = scheduledExecutorService.scheduleAtFixedRate(this::decrementRemainingTime, 1,1, TimeUnit.SECONDS);
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
        cancelTimerWhenTimeExpires();
    }


    private void updateCurrentRemainingTime(){
        minutesRemaining = currentRemainingTime / 60;
        secondsRemaining = currentRemainingTime % 60;
    }



    private void cancelTimerWhenTimeExpires(){
        if(currentRemainingTime <= 0){
            future.cancel(false);
        }
    }

}
