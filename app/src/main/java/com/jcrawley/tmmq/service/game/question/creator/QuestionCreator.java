package com.jcrawley.tmmq.service.game.question.creator;

import com.jcrawley.tmmq.service.game.level.OperationLimits;
import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

import java.util.Random;

public class QuestionCreator {

    private final MathOperation mathOperation;
    private OperationLimits operationLimits;
    private final Random random;
    private final String symbol;
    private String previousQuestionStr = "";
    int part1, part2;
    private int freshQuestionCount;
    private final int swapOffset;


    public QuestionCreator(MathOperation mathOperation, int swapOffset){
        this.mathOperation = mathOperation;
        this.symbol = mathOperation.getSymbol();
        random = new Random(System.nanoTime());
        this.swapOffset = swapOffset;
    }


    public QuestionCreator(MathOperation mathOperation){
        this(mathOperation, 0);
    }


    public void setOperationLimits(OperationLimits operationLimits){
        this.operationLimits = operationLimits;
    }


    public MathOperation getOperationType(){
        return mathOperation;
    }


    public OperationLimits getOperationLimits(){
        return operationLimits;
    }


    public MathQuestion createQuestion(){
        createParts();
        swapPartsIfLargeNumberShouldBeFirst();
        int result = mathOperation.perform(part1, part2);
        String text = createQuestionText(part1, part2);
        return createFreshQuestion(text, result);
    }


    MathQuestion createFreshQuestion(String text, int correctAnswer){
        if(text.equals(previousQuestionStr)){
            freshQuestionCount++;
            if(freshQuestionCount < 5){
                return createQuestion();
            }
        }
        previousQuestionStr = text;
        freshQuestionCount = 0;
        return  new MathQuestion(text, correctAnswer, mathOperation.containsExponent());
    }


    void createParts(){
        part1 = getRandomNumber(operationLimits.getFirstNumberMin(), operationLimits.getFirstNumberMax());
        part2 = getRandomNumber(operationLimits.getSecondNumberMin(), operationLimits.getSecondNumberMax());
    }


    public void swapPartsIfLargeNumberShouldBeFirst(){
        if(swapOffset > 0 && part2 > (part1 + swapOffset)){
            swapParts();
        }
    }


    private void swapParts(){
        int temp = part2;
        part2 = part1;
        part1 = temp;
    }


    private int getRandomNumber(int min, int max){
        return min == max ? max : min + getRandomRange(min, max);
    }


    private int getRandomRange(int min, int max){
       return random.nextInt((max + 1) - min);
    }


    String createQuestionText(int part1, int part2){
        return part1 + createSymbol()  + part2;
    }


    private String createSymbol(){
        return symbol.isEmpty() ? " " : " " + symbol + " ";
    }


    /* for testing only */
    public void setParts(int part1, int part2){
        this.part1 = part1;
        this.part2 = part2;
    }


    public int getPart1(){
        return part1;
    }


    public int getPart2(){
        return part2;
    }
}
