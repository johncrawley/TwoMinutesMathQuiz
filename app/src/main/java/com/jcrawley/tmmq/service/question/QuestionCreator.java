package com.jcrawley.tmmq.service.question;

import java.util.Random;
import java.util.function.BiFunction;

public class QuestionCreator {

    private final Random random;
    private final int numberMin, numberMax;
    final BiFunction<Integer, Integer, Integer> operationFunction;
    private final String symbol;
    private String previousQuestionStr = "";
    private final boolean isLargeNumberAlwaysFirst;
    int part1, part2;



    public QuestionCreator(String symbol, int numberMin, int numberMax, boolean isLargeNumberAlwaysFirst, BiFunction<Integer, Integer, Integer> operationFunction){
        this.symbol = symbol;
        this.numberMin = numberMin;
        this.numberMax = numberMax;
        this.operationFunction = operationFunction;
        this.isLargeNumberAlwaysFirst = isLargeNumberAlwaysFirst;
        random = new Random(System.nanoTime());
    }


    public QuestionCreator(String symbol, int numberMin, int numberMax, BiFunction<Integer, Integer, Integer> operationFunction){
        this(symbol, numberMin, numberMax, false, operationFunction);
    }


    public MathQuestion createQuestion(){
        createParts();
        swapPartsIfLargeNumberShouldBeFirst();
        int result = operationFunction.apply(part1, part2);
        String text = createQuestionText(part1, part2);
        return createFreshQuestion(text, result);
    }


    MathQuestion createFreshQuestion(String text, int result){
        if(text.equals(previousQuestionStr)){
            return createQuestion();
        }
        previousQuestionStr = text;
        return  new MathQuestion(text, result);
    }


    void createParts(){
        part1 = getRandomNumber();
        part2 = getRandomNumber();
    }


    void swapPartsIfLargeNumberShouldBeFirst(){
        if(isLargeNumberAlwaysFirst && part2 > part1){
            int temp = part2;
            part2 = part1;
            part1 = temp;
        }
    }


    private int getRandomNumber(){
        return Math.max(numberMin, random.nextInt(numberMax));
    }


    String createQuestionText(int part1, int part2){
        return part1 + " " + symbol + " "  + part2;
    }

}
