package com.jcrawley.tmmq;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GameService extends Service {
    int startMode;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used
    private MainActivity mainActivity;



    public void setQuestionText(String questionText){

    }


    public void notifyIncorrectAnswer(){

    }


    public void updateScore(int score){

    }

    public void updateTimer(int minutesRemaining, int secondsRemaining){

    }


    @Override
    public void onCreate() {
        // The service is being created
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return startMode;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return allowRebind;
    }


    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }


    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public class LocalBinder extends Binder {
        GameService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return GameService.this;
        }
    }

}
