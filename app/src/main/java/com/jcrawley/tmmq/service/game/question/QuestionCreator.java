package com.jcrawley.tmmq.service.game.question;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.level.OperationLimits;

import java.util.Random;
import java.util.function.BiFunction;

public class QuestionCreator {

    private final MathOperation mathOperation;
    private OperationLimits operationLimits;
    private final Random random;
    final BiFunction<Integer, Integer, Integer> operationFunction;
    private final String symbol;
    private String previousQuestionStr = "";
    private final boolean isLargeNumberAlwaysFirst;
    int part1, part2;



    public QuestionCreator(MathOperation mathOperation, String symbol, boolean isLargeNumberAlwaysFirst, BiFunction<Integer, Integer, Integer> operationFunction){
        this.mathOperation = mathOperation;
        this.symbol = symbol;
        this.operationFunction = operationFunction;
        this.isLargeNumberAlwaysFirst = isLargeNumberAlwaysFirst;
        random = new Random(System.nanoTime());
    }


    public QuestionCreator(MathOperation mathOperation, String symbol, BiFunction<Integer, Integer, Integer> operationFunction){
        this(mathOperation, symbol, false, operationFunction);
    }


    public void setGameLevel(GameLevel gameLevel){
        operationLimits = gameLevel.getOperationLimitsFor(mathOperation);
    }


    public MathQuestion createQuestion(){
        createParts();
        swapPartsIfLargeNumberShouldBeFirst();
        int result = operationFunction.apply(part1, part2);
        String text = createQuestionText(part1, part2);
        return createFreshQuestion(text, result);
    }


    public MathOperation getMathOperation(){
        return mathOperation;
    }


    MathQuestion createFreshQuestion(String text, int result){
        if(text.equals(previousQuestionStr)){
            return createQuestion();
        }
        previousQuestionStr = text;
        return  new MathQuestion(text, result);
    }


    void createParts(){
        part1 = getRandomNumber(operationLimits.getFirstNumberMin(), operationLimits.getFirstNumberMax());
        part2 = getRandomNumber(operationLimits.getSecondNumberMin(), operationLimits.getSecondNumberMax());
    }


    void swapPartsIfLargeNumberShouldBeFirst(){
        if(isLargeNumberAlwaysFirst && part2 > part1){
            int temp = part2;
            part2 = part1;
            part1 = temp;
        }
    }


    private int getRandomNumber(int min, int max){
        log("getRandomNumber() min max: " + min + "," + max);
        return Math.max(min, random.nextInt(Math.max(1, max)));
    }

    private void log(String msg){
        System.out.println("^^^ Question Creator: " + msg);
    }


    String createQuestionText(int part1, int part2){
        return part1 + " " + symbol + " "  + part2;
    }

}
