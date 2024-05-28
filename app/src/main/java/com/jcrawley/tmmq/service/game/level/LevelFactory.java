package com.jcrawley.tmmq.service.game.level;

import static com.jcrawley.tmmq.service.game.question.MathOperation.ADDITION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.DIVISION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.MULTIPLICATION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.POWER_OF;
import static com.jcrawley.tmmq.service.game.question.MathOperation.SUBTRACTION;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.QuestionCreator;
import com.jcrawley.tmmq.service.game.question.QuestionCreatorForDivision;
import com.jcrawley.tmmq.service.game.question.QuestionCreatorForSubtraction;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    private static Map<Integer, GameLevel> levels;


    public static Map<Integer, GameLevel> createLevels(){
        levels = new HashMap<>();
        addLevel(1,
                addition(1,5,1,5),
                subtraction(1, 4, 1,6));

        addLevel(2,
                addition(2,10,3,10),
                subtraction(2, 6, 1,6));

        addLevel(3,
                addition(10,20,5,12),
                subtraction(5, 9, 3,10));

        addLevel(4,
                addition(20,50,8,30),
                subtraction(6, 12, 8,15),
                division(2,2,10),
                division(3,5,2,5),
                multiplication(2,10,2,5),
                powerOf(2,5,2,2));

        addLevel(5,
                addition(50,105,30,135),
                subtraction(6, 15, 15,30),
                multiplication(3,12,5,12),
                division(2,8,32),
                division(3,6,3,9),
                powerOf(2,5,3,3),
                powerOf(4,10,2,2));

        addLevel(6,
                addition(100,200,70,170),
                subtraction(12, 20, 21,45),
                multiplication(5,13,5,12),
                multiplication(26,50,2,3),
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
                multiplication(7,14,8,12),
                multiplication(30,60,2,5),
                division(2,60,120),
                division(3,4,28,48),
                division(5,6,15,24),
                division(7,12,8,14),
                powerOf(2,5,6),
                powerOf(3,9,3,3),
                powerOf(8,12,2,2));

        addLevel(8,
                addition(800,1500,800,1300),
                subtraction(50, 150, 150,220),
                multiplication(9,15,9,15),
                multiplication(70,120,5,9),
                division(2,100,200),
                division(3,5,40,80),
                division(6,8,22,50),
                division(9,11,12,20),
                division(12,14,7,15),
                powerOf(2,6,8),
                powerOf(3,5,3,4),
                powerOf(6,8,3,3),
                powerOf(9,13,2,2));

        addLevel(9,
                addition(1500,3500,1500,2550),
                subtraction(150, 800, 200,900),
                multiplication(11,16,11,16),
                multiplication(100,200,7,11),
                division(9,18,5,18),
                powerOf(2,7,9),
                powerOf(3,4,4,5),
                powerOf(5,10,3,3),
                powerOf(11,15,2,2));

        addLevel(10,
                addition(5_000,10_000,3_000,8_000),
                subtraction(300, 990, 1000,3000),
                multiplication(8,15,15,20),
                multiplication(12,20,11,15),
                multiplication(200,300,11,15),
                division(12,20,12,20),
                powerOf(2,8,12),
                powerOf(3,5,4,5),
                powerOf(6,12,3,3),
                powerOf(15,20,2,2));

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
            case DIVISION -> new QuestionCreatorForDivision();
            case SUBTRACTION -> new QuestionCreatorForSubtraction();
            default -> new QuestionCreator(mathOperation);
        };
    }

}
