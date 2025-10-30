package com.jcrawley.tmmq.service.preferences;

 public interface GamePreferenceManager {

     void saveTimer(int value);
     int getTimer();
     void saveTimerIndex(int value);
     int getTimerIndex();
     void saveLevel(int value);
    int getLevel();


}
