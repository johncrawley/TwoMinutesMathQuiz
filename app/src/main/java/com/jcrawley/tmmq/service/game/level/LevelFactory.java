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
                addition(4,12,5,12),
                subtraction(5, 9, 3,10));
        addLevel(4,
                addition(10,20,8,20),
                subtraction(6, 12, 8,15),
                multiplication(2,10,2,5));
        addLevel(5,
                addition(12,35,12,35),
                subtraction(6, 15, 15,30),
                multiplication(3,12,5,12),
                division(2,6,2,10),
                powerOf(2,10,2,2)
                );

        addLevel(6,
                addition(12,35,12,35),
                subtraction(12, 20, 21,45),
                multiplication(5,13,5,13),
                division(3,8,2,12));
        addLevel(7,
                addition(12,35,12,35),
                subtraction(20, 50, 35,70),
                multiplication(7,14,8,14),
                division(5,12,3,12));
        addLevel(8,
                addition(12,35,12,35),
                subtraction(50, 150, 150,220),
                multiplication(9,15,9,15),
                division(7,13,4,13));
        addLevel(9,
                addition(500,1500,300,1450),
                subtraction(150, 800, 200,900),
                multiplication(11,16,11,16),
                division(9,18,5,18));
        addLevel(10,
                addition(10_000,20_000,8_000,18_000),
                subtraction(10_000, 15_000, 3000,9000),
                multiplication(12,20,15,35),
                division(12,20,12,20));
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


    private static OperationLimits powerOf(int minNumber, int maxNumber, int minExponent, int maxExponent){
        return new OperationLimits(POWER_OF, minNumber, maxNumber, minExponent, maxExponent);
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
