package com.jcrawley.tmmq;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;



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
    }


    public void setQuestionText(String questionText){

    }


    public void notifyIncorrectAnswer(){

    }


    public void updateScore(int score){

    }


    public void updateTimer(int minutesRemaining, int secondsRemaining){
        mainActivity.updateTime(minutesRemaining, secondsRemaining);
    }


    public void startGame(){
        game.startGame();
    }


    @Override
    public void onCreate() {
        game = new Game(this);
        game.init();
        log("entered onCreate()");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Entered onStartCommand()");
        return startMode;
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
