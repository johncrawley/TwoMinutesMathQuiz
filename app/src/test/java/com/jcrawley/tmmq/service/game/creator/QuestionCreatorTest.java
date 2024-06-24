package com.jcrawley.tmmq.service.game.creator;

import static org.junit.Assert.assertEquals;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;

import org.junit.Before;
import org.junit.Test;

public class QuestionCreatorTest {

    private QuestionCreator questionCreator;
    private int swapOffset = 10;

    @Before
    public void init(){
    questionCreator = new QuestionCreator(MathOperation.MULTIPLICATION, swapOffset);
    }

    @Test
    public void partsGetSwapped(){
        int num1 = 5;
        int num2 = 6;
        questionCreator.setParts(num1, num2);
        questionCreator.swapPartsIfLargeNumberShouldBeFirst();
        assertEquals(num1, questionCreator.getPart1());
        assertEquals(num2, questionCreator.getPart2());

        int num3 = 100;
        int num4 = 50;
        questionCreator.setParts(num3, num4);
        questionCreator.swapPartsIfLargeNumberShouldBeFirst();
        assertEquals(num3, questionCreator.getPart1());
        assertEquals(num4, questionCreator.getPart2());


        int num5 = 8;
        int num6 = 25;
        questionCreator.setParts(num5, num6);
        questionCreator.swapPartsIfLargeNumberShouldBeFirst();
        assertEquals(num6, questionCreator.getPart1());
        assertEquals(num5, questionCreator.getPart2());
    }
}
