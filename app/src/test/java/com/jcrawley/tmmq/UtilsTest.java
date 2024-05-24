package com.jcrawley.tmmq;

import static org.junit.Assert.assertEquals;

import com.jcrawley.tmmq.utils.Utils;

import org.junit.Test;

import java.util.List;

public class UtilsTest {

    @Test
    public void canConvertTimeValue(){

        List<Integer> values = List.of(30, 50, 60, 120, 180, 240);
        List<String> expectedValues = List.of("0:30", "0:50", "1:00", "2:00", "3:00", "4:00");
        for(int i = 0; i < values.size(); i++){
            int value  = values.get(i);
            String convertedStr = Utils.getTimerStrFor(value);
            assertEquals(expectedValues.get(i), convertedStr);
        };
    }
}
