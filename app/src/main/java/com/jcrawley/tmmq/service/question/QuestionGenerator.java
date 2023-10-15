package com.jcrawley.tmmq.service.question;

import com.jcrawley.tmmq.service.question.MathQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {


    private final Random random;
    private String previousQuestionStr = "";
    private final List<MathOperation> mathOperations;
    private final MathOperation addOperation;

    public QuestionGenerator(){

        random = new Random(System.currentTimeMillis());
        mathOperations = new ArrayList<>();
        addOperation = new MathOperation("+", Integer::sum);
        mathOperations.add(addOperation);
        mathOperations.add(new MathOperation("-", (x, y) -> x - y));
    }


    public MathQuestion generate(){
        int limit1 = 10;
        int limit2 = 10;
        int part1 = getRandomNumber(limit1);
        int part2 = getRandomNumber(limit2);
        MathOperation operation = getRandomMathOperation();
        int result = operation.performOperation(part1, part2);
        String text = createQuestionText(part1, part2, operation);
        if(text.equals(previousQuestionStr)){
            return generate();
        }
        previousQuestionStr = text;
        return  new MathQuestion(text, result);
    }


    private String createQuestionText(int part1, int part2, MathOperation mathOperation){
        return part1 + mathOperation.getSymbol()  + part2;
    }


    private MathOperation getRandomMathOperation(){
        return mathOperations.get(random.nextInt(mathOperations.size()));
    }


    private int getRandomNumber(int limit){
        return Math.max(1, random.nextInt(limit));
    }


}
