package com.jcrawley.tmmq.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.preferences.GamePreferenceManager;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.service.sound.SoundPlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private MainActivity mainActivity;
    private final Game game;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> notifyGameOverFuture;
    private SoundPlayer soundPlayer;
    GamePreferenceManager gamePreferenceManager;


    public GameService() {
        super();
        game = new Game();
    }


    public void playSound(Sound sound){
        soundPlayer.playSound(sound);
    }


    public SharedPreferences getScorePrefs(){
        return getSharedPreferences("score_preferences", MODE_PRIVATE);
    }


    public void setTimer(int value){
        if(gamePreferenceManager != null){
            gamePreferenceManager.saveTimer(value);
        }
        if(game != null){
            game.setTimerLength(value);
        }
    }

    public void resetTimer(){
        game.resetTimer();
    }


    public void setTimer(int value, int currentTimerIndex){
        if(gamePreferenceManager != null){
            gamePreferenceManager.saveTimer(value);
            gamePreferenceManager.saveTimerIndex(currentTimerIndex);
        }
        if(game != null){
            game.setTimerLength(value);
        }
    }


    public int getTimer(){
        if(gamePreferenceManager != null){
            return gamePreferenceManager.getTimer();
        }
        return 120;
    }


    public int getSavedTimerIndex(){
        if(gamePreferenceManager != null){
            return gamePreferenceManager.getTimerIndex();
        }
        return 0;
    }


    public void setLevel(int value){
        if(gamePreferenceManager != null){
            gamePreferenceManager.saveLevel(value);
        }
        if(game != null){
            game.setDifficulty(value);
        }
    }


    public int getLevel(){
        if(gamePreferenceManager != null){
            return gamePreferenceManager.getLevel();
        }
        return 5;
    }


    public void setQuestionOnView(MathQuestion question){
        if(mainActivity == null){
            return;
        }
        mainActivity.setQuestion(question);
    }


    public void quitGame(){
        game.quit();
    }


    public void resetScore(){
        if(mainActivity != null) {
            mainActivity.setScore(0);
        }
    }


    public void notifyIncorrectAnswer(){
        mainActivity.notifyIncorrectAnswer();
    }


    public void updateScore(int score){
        if(mainActivity != null){
            mainActivity.setScore(score);
        }
    }


    public void updateTimer(int minutesRemaining, int secondsRemaining){
        if(mainActivity != null){
            mainActivity.setTimeRemaining(minutesRemaining, secondsRemaining);
        }
    }


    public void onGameOver(ScoreStatistics scoreStatistics){
       ScoreStatistics fullScoreStats = new ScoreRecords(getScorePrefs()).getCompleteScoreStatsAndSaveRecords(scoreStatistics);
       notifyGameOverFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> mainActivity.onGameOver(fullScoreStats), 0, 2, TimeUnit.SECONDS);
    }


    public void notifyThatGameFinished(){
        notifyGameOverFuture.cancel(false);
    }


    public void startGame(){
        game.startGame();
    }


    public void setGameLevel(int levelNumber){
        game.setDifficulty(levelNumber);
    }


    @Override
    public void onCreate() {
        game.init(this);
        soundPlayer = new SoundPlayer(getApplicationContext());
        gamePreferenceManager = new GamePreferenceManager(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onRebind(Intent intent) {
    }


    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void submitAnswer(String answerText){
        game.checkAnswer(answerText);
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}
