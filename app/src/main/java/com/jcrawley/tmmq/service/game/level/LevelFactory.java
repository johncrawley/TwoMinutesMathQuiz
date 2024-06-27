package com.jcrawley.tmmq.service.game.level;

import static com.jcrawley.tmmq.service.game.question.MathOperation.ADDITION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.DIVISION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.MULTIPLICATION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.POWER_OF;
import static com.jcrawley.tmmq.service.game.question.MathOperation.SUBTRACTION;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.creator.MultiplicationQuestionCreator;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;
import com.jcrawley.tmmq.service.game.question.creator.DivisionQuestionCreator;
import com.jcrawley.tmmq.service.game.question.creator.SubtractionQuestionCreator;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    private static Map<Integer, GameLevel> levels;


    public static Map<Integer, GameLevel> createLevels(){
        levels = new HashMap<>();
        long start = System.currentTimeMillis();
        addLevel(1,
                addition(1,5,1,5),
                subtraction(1, 4, 1,6));

        addLevel(2,
                addition(2,10,3,10),
                subtraction(2, 6, 1,6));

        addLevel(3,
                addition(10,20,5,12),
                multiplication(2,2,10),
                subtraction(5, 9, 3,10));

        addLevel(4,
                addition(20,50,8,30),
                subtraction(6, 12, 8,15),
                division(2,2,10),
                division(3,5,2,5),
                multiplication(2,5,12),
                multiplication(3,10,2,5),
                powerOf(2,2, 3),
                powerOf(3,6,2,2));

        addLevel(5,
                addition(50,105,30,135),
                subtraction(6, 15, 15,30),
                multiplication(2,12,24),
                multiplication(3,5,18),
                multiplication(4,12,5,12),
                division(2,8,32),
                division(3,6,3,9),
                powerOf(2,5,3,3),
                powerOf(4,10,2,2));

        addLevel(6,
                addition(100,200,70,170),
                subtraction(12, 20, 21,45),
                multiplication(2,22,90),
                multiplication(3,7,15,45),
                multiplication(8,12,8,13),
                division(2,20,64),
                division(3,15,32),
                division(4,12,24),
                division(5,8,6,12),
                powerOf(2,4,5),
                powerOf(3,7,3,3),
                powerOf(6,12,2,2));

        addLevel(7,
                addition(200,590,290,590),
                subtraction(20, 50, 35,70),
                multiplication(2, 4,80,250),
                multiplication(5,9,30,80),
                multiplication(10,12,20,30),
                multiplication(13,15,13,15),
                division(2,60,120),
                division(3,4,28,48),
                division(5,6,15,24),
                division(7,12,8,14),
                powerOf(2,5,7),
                powerOf(3,9,3,3),
                powerOf(8,12,2,2));

        addLevel(8,
                addition(800,1500,800,1300),
                subtraction(50, 150, 150,220),
                multiplication(2, 4,250,500),
                multiplication(5,9,70,120),
                multiplication(10,12,40,60),
                multiplication(13,16,16,22),
                division(2,100,200),
                division(3,5,40,80),
                division(6,8,22,50),
                division(9,11,12,20),
                division(12,14,5,15),
                powerOf(2,6,8),
                powerOf(3,5,3,4),
                powerOf(6,8,3,3),
                powerOf(9,13,2,2));

        addLevel(9,
                addition(1500,3500,1500,2550),
                subtraction(150, 800, 200,900),
                multiplication(2, 4,500,800),
                multiplication(5,9,90,160),
                multiplication(11,12,50,80),
                multiplication(13,16,20,25),
                division(2,200,500),
                division(3,5,100,250),
                division(6,8,45,80),
                division(9,11,18,30),
                division(12,14,12,18),
                division(15,5,12),
                powerOf(2,7,9),
                powerOf(3,4,4,5),
                powerOf(5,10,3,3),
                powerOf(11,15,2,2));

        addLevel(10,
                addition(5_000,10_000,3_000,8_000),
                subtraction(300, 990, 1000,3000),
                multiplication(2, 1000,5000),
                multiplication(3,4, 800,1500),
                multiplication(5,9,150,350),
                multiplication(11,12,70,100),
                multiplication(13,17,23,30),
                division(2,450,2000),
                division(3,5,230,500),
                division(6,8,70,120),
                division(9,11,25,50),
                division(12,14,16,22),
                division(15,12,19),
                powerOf(2,8,12),
                powerOf(3,5,4,5),
                powerOf(6,12,3,3),
                powerOf(15,20,2,2));

        long duration = System.currentTimeMillis() - start;
        System.out.println("^^^ LevelFactory: duration: " + duration);
        return levels;
    }


    private static OperationLimits addition(int min1, int max1, int min2, int max2){
        return new OperationLimits(ADDITION, min1, max1, min2, max2);
    }


    private static OperationLimits subtraction(int subtractionMin, int subtractionMax, int ansMin, int ansMax){
        return new OperationLimits(SUBTRACTION, subtractionMin, subtractionMax, ansMin, ansMax);
    }


    private static OperationLimits multiplication(int min1, int max1, int min2, int max2){
        return new OperationLimits(MULTIPLICATION, min1, max1, min2, max2);
    }


    private static OperationLimits multiplication(int number1, int min2, int max2){
        return multiplication( number1, number1, min2, max2);
    }


    private static OperationLimits division(int divisorMin, int divisorMax, int ansMin, int ansMax){
        return new OperationLimits(DIVISION, divisorMin, divisorMax, ansMin, ansMax);
    }

    private static OperationLimits division(int divisor, int ansMin, int ansMax){
        return division(divisor, divisor, ansMin, ansMax);
    }


    private static OperationLimits powerOf(int minNumber, int maxNumber, int minExponent, int maxExponent){
        return new OperationLimits(POWER_OF, minNumber, maxNumber, minExponent, maxExponent);
    }


    private static OperationLimits powerOf(int number, int minExponent, int maxExponent){
        return  powerOf(number, number, minExponent, maxExponent);
    }


    private static void addLevel(int difficulty, OperationLimits ... operationLimits){
        GameLevel gameLevel = new GameLevel(difficulty);
        for(OperationLimits limits : operationLimits){
            QuestionCreator qc = generateQuestionCreatorFor(limits.getMathOperation());
            qc.setOperationLimits(limits);
            gameLevel.addQuestionCreator(qc);
        }
        if(levels.containsKey(difficulty)){
            throw new RuntimeException("Difficulty level already added!");
        }
        levels.put(difficulty, gameLevel);
    }


    private static QuestionCreator generateQuestionCreatorFor(MathOperation mathOperation){
        return switch(mathOperation){
            case DIVISION -> new DivisionQuestionCreator();
            case SUBTRACTION -> new SubtractionQuestionCreator();
            case MULTIPLICATION -> new MultiplicationQuestionCreator();
            default -> new QuestionCreator(mathOperation);
        };
    }

}
