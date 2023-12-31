package com.jcrawley.tmmq.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.TimerLength;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

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


    public GameService() {
        super();
        game = new Game();
    }



    public SharedPreferences getScorePrefs(){
        final String SCORE_PREFERENCES = "score_preferences";
        return getSharedPreferences(SCORE_PREFERENCES, MODE_PRIVATE);
    }



    public void setQuestionTextOnView(String questionText){
        if(mainActivity == null){
            return;
        }
        mainActivity.setQuestionText(questionText);
    }


    public void quitGame(){
        game.quit();
    }


    public void resetScore(){
        if(mainActivity != null) {
            mainActivity.setScore(0);
        }
    }


    public void fadeInQuestionTextOnView(String questionText){
        if(mainActivity == null){
            return;
        }
        mainActivity.fadeInQuestionText(questionText);
    }


    public void notifyIncorrectAnswer(){
        mainActivity.notifyIncorrectAnswer();
    }


    public void setupPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences("ScorePreferences", MODE_PRIVATE);
    }




    public void updateScore(int score){
        if(mainActivity == null){
            return;
        }
        mainActivity.setScore(score);
    }


    public void updateTimer(int minutesRemaining, int secondsRemaining){
        if(mainActivity == null){
            return;
        }
        mainActivity.setTimeRemaining(minutesRemaining, secondsRemaining);
    }



    public void onGameOver(ScoreStatistics scoreStatistics){
       ScoreStatistics fullScoreStats = new ScoreRecords(getScorePrefs()).getCompleteScoreStatsAndSaveRecords(scoreStatistics);
       notifyGameOverFuture = scheduledExecutorService.scheduleAtFixedRate(()-> mainActivity.onGameOver(fullScoreStats), 0, 2, TimeUnit.SECONDS);
    }


    public void notifyThatGameFinished(){
        notifyGameOverFuture.cancel(false);
    }


    public void startGame(TimerLength timerLength){
        game.startGame(timerLength);
    }


    public void setGameLevel(int levelNumber){
        game.setDifficulty(levelNumber);
    }


    @Override
    public void onCreate() {
        log("entered onCreate()");
        game.init(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }

    private void log(String msg){
        System.out.println("^^^ GameService: " + msg);
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
        log("Entered onDestroy() setting activity ref to null");
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void submitAnswer(String answerText){
        log("entered submitAnswer() answer: " + answerText);
        game.checkAnswer(answerText);
    }


    public void setActivity(MainActivity mainActivity){
        log("Entered setActivity()");
        this.mainActivity = mainActivity;
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}
