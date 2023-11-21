package com.jcrawley.tmmq.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.game.Game;
import com.jcrawley.tmmq.service.game.level.GameLevel;

import java.time.LocalDateTime;
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


    private enum RecordType { DAILY, ALL_TIME }
    private final String SCORE_PREFERENCES = "score_preferences";
    private final String LAST_RECORD_DATE_KEY = "last_record_date";



    public GameService() {
        super();
        game = new Game();
    }


    public void setQuestionTextOnView(String questionText){
        if(mainActivity == null){
            return;
        }
        mainActivity.setQuestionText(questionText);
    }


    public void fadeInQuestionTextOnView(String questionText){
        if(mainActivity == null){
            return;
        }
        mainActivity.fadeInQuestionText(questionText);
    }


    public void notifyIncorrectAnswer(){

    }


    public void setupPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences("ScorePreferences", MODE_PRIVATE);
    }



    public SharedPreferences getScorePrefs(){
        return getSharedPreferences(SCORE_PREFERENCES, MODE_PRIVATE);
    }


    private void getHighScore(){
        
    }


    private String createScorePrefKey(Game game){
        GameLevel gameLevel = game.getCurrentLevel();
        int timerLength = game.getInitialTimer();
        return "scoreFor_" + gameLevel.toString() + "_"  + timerLength;
    }


    private String createScorePrefKey(RecordType recordType, GameLevel gameLevel, int timerLength){
        return "scoreFor_" + recordType.toString() + "_" + gameLevel.toString() + "_"  + timerLength;
    }


    private boolean saveHighScore(Game game, int finalScore, RecordType recordType){
        SharedPreferences scorePrefs = getScorePrefs();
        String allTimeKey = createScorePrefKey(recordType, game.getCurrentLevel(), game.getInitialTimer());
        int allTimeScore = scorePrefs.getInt(allTimeKey, 0);
        if(finalScore > allTimeScore){
            scorePrefs.edit().putInt(allTimeKey, finalScore).apply();
            return true;
        }
        return false;
    }




    private boolean saveDailyHighScore(Game game, int finalScore){
        SharedPreferences scorePrefs = getScorePrefs();
        String todayDateStr = LocalDateTime.now().toString();
        String allTimeKey = createScorePrefKey(RecordType.DAILY, game.getCurrentLevel(), game.getInitialTimer());
        String lastDateStr = scorePrefs.getString(LAST_RECORD_DATE_KEY, todayDateStr);
        if(!lastDateStr.equals(todayDateStr)){
            scorePrefs.edit().putInt(allTimeKey, finalScore).apply();
            return true;
        }

        int allTimeScore = scorePrefs.getInt(allTimeKey, 0);
        if(finalScore > allTimeScore){
            scorePrefs.edit().putInt(allTimeKey, finalScore).apply();
            return true;
        }
        return false;
    }


    private void saveDate(SharedPreferences sharedPreferences, String dateStr){
        sharedPreferences.edit().putString(LAST_RECORD_DATE_KEY, dateStr).apply();
    }

    private void saveScore(SharedPreferences sharedPreferences, String key, int score){
        sharedPreferences.edit().putInt(key, score).apply();
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


    public void onGameOver(int finalScore){
       notifyGameOverFuture = scheduledExecutorService.scheduleAtFixedRate(()-> mainActivity.onGameOver(finalScore), 0, 2, TimeUnit.SECONDS);
    }


    public void notifyThatGameFinished(){
        notifyGameOverFuture.cancel(false);
    }


    public void startGame(int timerLength){
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
