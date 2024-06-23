package com.jcrawley.tmmq.service.score;

import java.time.LocalDateTime;

public class CurrentDateGeneratorImpl implements CurrentDateGenerator{

    @Override
    public String get() {
        LocalDateTime dateToday = LocalDateTime.now();
        return  dateToday.getDayOfMonth()
                + "-" + dateToday.getMonthValue()
                + "-" + dateToday.getYear();
    }
}
