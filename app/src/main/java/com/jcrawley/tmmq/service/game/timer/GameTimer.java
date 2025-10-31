package com.jcrawley.tmmq.service.game.timer;

import com.jcrawley.tmmq.service.game.Game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameTimer{

    private ScheduledFuture<?> future;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Game game;
    private final GameTimerModel gameTimerModel;

    public GameTimer(Game game, GameTimerModel gameTimerModel){
        this.gameTimerModel = gameTimerModel;
        this.game = game;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        gameTimerModel.calculateMinutesAndSeconds();
        if(gameTimerModel.shouldTimerBeRunning()){
            start();
        }
    }


    public void start(){
        gameTimerModel.start();
        future = scheduledExecutorService.scheduleWithFixedDelay(this::decrementRemainingTime, 1,1, TimeUnit.SECONDS);
        gameTimerModel.saveFuture(future);
    }


    public void setTimerLength(int timerLength){
        gameTimerModel.setInitialRemainingTime(timerLength);
        resetTime();
        updateTimer();
    }


    public void resetTime(){
        gameTimerModel.resetTime();
    }


    private void decrementRemainingTime(){
        gameTimerModel.updateCurrentRemainingTime();
        updateTimer();
        cancelTimerWhenTimeExpires();
    }


    public void updateTimer(){
        game.updateTime(gameTimerModel.getMinutesRemaining(), gameTimerModel.getSecondsRemaining());
    }


    private void cancelTimerWhenTimeExpires(){
        if(gameTimerModel.isTimeUp()){
            cancel();
            game.gameOver();
        }
    }


    public void cancel(){
        gameTimerModel.stop();
        if(future != null && !future.isCancelled() && !future.isDone()){
            future.cancel(false);
        }
    }


}