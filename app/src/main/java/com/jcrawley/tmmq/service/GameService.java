package com.jcrawley.tmmq.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.game.Game;


public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private MainActivity mainActivity;
    private final Game game;


    public GameService() {
        super();
        game = new Game();
    }


    public void setQuestionTextOnView(String questionText){
        mainActivity.setQuestionText(questionText);
    }


    public void fadeInQuestionTextOnView(String questionText){
        mainActivity.fadeInQuestionText(questionText);
    }


    public void notifyIncorrectAnswer(){

    }


    public void updateScore(int score){
        mainActivity.setScore(score);
    }


    public void updateTimer(int minutesRemaining, int secondsRemaining){
        mainActivity.setTimeRemaining(minutesRemaining, secondsRemaining);
    }


    public void onGameOver(int finalScore){
        mainActivity.onGameOver(finalScore);
    }


    public void startGame(int timerLength){
        game.startGame(timerLength);
    }


    public void setGameLevel(int levelNumber){
        game.setDifficulty(levelNumber);
    }


    public boolean isGameStarted(){
        return game.isStarted();
    }

    public int getScore(){
        return game.getScore();
    }


    public String getQuestionText(){
        return game.getCurrentQuestionText();
    }


    public int getSecondsRemaining(){
        return game.getSecondsRemaining();
    }


    public int getMinutesRemaining(){
        return game.getMinutesRemaining();
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
