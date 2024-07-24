package com.jcrawley.tmmq.service.game.creator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void randomNumberGeneratorEventuallyGoesThroughFullRange(){
        try {
            Method method = QuestionCreator.class.getDeclaredMethod("getRandomNumber", int.class, int.class);
            method.setAccessible(true);
            Set<Integer> existingResults = new HashSet<>();
            int min = 1;
            int max = 20;
            int numberOfCycles = 200;
            for(int i = 0; i < numberOfCycles; i ++){
                Integer result =(Integer)method.invoke(questionCreator, min, max);
                if(result != null){
                    existingResults.add(result);
                }
            }
            for(int i = 1; i <= max; i++){
                assertTrue(existingResults.contains(i));
            }
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            handleException(e);
        }
    }


    @Test
    public void upperRandomLimitsAreCalculatedCorrectly(){

        try {
            Method method = QuestionCreator.class.getDeclaredMethod("getRandomRange", int.class, int.class);
            method.setAccessible(true);
            int min = 5;
            int max = 10;
            int numberOfCycles = 100;
            for(int i = 0; i < numberOfCycles; i ++){
                Integer result =(Integer)method.invoke(questionCreator, min, max);
                if(result != null){
                    assertTrue((int)result <= max - min);
                }
            }
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            handleException(e);
        }
    }


    public void handleException(Exception e){

    }
}
