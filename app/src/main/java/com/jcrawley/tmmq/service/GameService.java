package com.jcrawley.tmmq.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.tmmq.MainActivity;


public class GameService extends Service {
    int startMode;       // indicates how to behave if the service is killed
    IBinder binder = new LocalBinder();   // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used
    private MainActivity mainActivity;
    private Game game;


    public GameService(String name) {
        super();
    }


    public GameService() {
        super();
        log("Entered constructor");
        game = new Game(this);
        game.init();
    }


    public void setQuestionText(String questionText){
        mainActivity.setQuestionText(questionText);
    }


    public void notifyIncorrectAnswer(){

    }


    public void updateScore(int score){
        mainActivity.updateScore(score);
    }


    public void updateTimer(int minutesRemaining, int secondsRemaining){
        mainActivity.updateTime(minutesRemaining, secondsRemaining);
    }


    public void startGame(){
        game.startGame();
    }


    @Override
    public void onCreate() {
        log("entered onCreate()");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Entered onStartCommand()");
        //return startMode;
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }

    private void log(String msg){
        System.out.println("^^^ GameService: " + msg);
    }


    @Override
    public IBinder onBind(Intent intent) {
        log("entered onBind()");
        return binder;
    }



    @Override
    public boolean onUnbind(Intent intent) {
        mainActivity = null;
        return allowRebind;
    }


    @Override
    public void onRebind(Intent intent) {
    }


    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public void submitAnswer(String answerText){
        game.checkAnswer(answerText);
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            log("Entered LocalBinder.getService()");
            return GameService.this;
        }
    }

}
