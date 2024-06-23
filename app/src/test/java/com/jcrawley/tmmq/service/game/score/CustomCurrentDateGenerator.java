package com.jcrawley.tmmq.service.game.score;

import com.jcrawley.tmmq.service.score.CurrentDateGenerator;

public class CustomCurrentDateGenerator implements CurrentDateGenerator {

    private String currentDate = "today";

    @Override
    public String get() {
        return currentDate;
    }

    public void setCurrentDate(String amendedDate){
        this.currentDate = amendedDate;
    }
}
