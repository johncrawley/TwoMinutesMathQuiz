package com.jcrawley.tmmq.service.game.level;

import static com.jcrawley.tmmq.service.game.question.MathOperation.ADDITION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.DIVISION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.MULTIPLICATION;
import static com.jcrawley.tmmq.service.game.question.MathOperation.SUBTRACTION;

import com.jcrawley.tmmq.service.game.question.MathOperation;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {


    public static Map<Integer, GameLevel> createAndAddLevels(){

        Map<Integer, GameLevel> levels = new HashMap<>(10);

        addTo(levels,1,
                new OperationLimits(ADDITION, 1,5,1,5),
                new OperationLimits(SUBTRACTION, 1, 10, 1,2));

        addTo(levels,2,
                new OperationLimits(ADDITION, 2,10,3,10),
                new OperationLimits(SUBTRACTION, 5, 12, 2,7));

        addTo(levels,3,
                new OperationLimits(ADDITION, 4,12,5,12),
                new OperationLimits(SUBTRACTION, 15, 20, 5,12));

        addTo(levels,4,
                new OperationLimits(ADDITION, 10,20,8,20),
                new OperationLimits(SUBTRACTION, 20, 50, 12,35),
                new OperationLimits(MULTIPLICATION, 2,10,2,5));

        addTo(levels,5,
                new OperationLimits(ADDITION, 12,35,12,35),
                new OperationLimits(SUBTRACTION, 30, 70, 15,39),
                new OperationLimits(MULTIPLICATION, 3,12,5,12),
                new OperationLimits(DIVISION, 2,12,2,12));

        addTo(levels,6,
                new OperationLimits(ADDITION, 12,35,12,35),
                new OperationLimits(SUBTRACTION, 30, 70, 15,39),
                new OperationLimits(MULTIPLICATION, 3,12,5,12),
                new OperationLimits(DIVISION, 2,12,2,12));

        addTo(levels,7,
                new OperationLimits(ADDITION, 12,35,12,35),
                new OperationLimits(SUBTRACTION, 30, 70, 15,39),
                new OperationLimits(MULTIPLICATION, 3,12,5,12),
                new OperationLimits(DIVISION, 2,12,2,12));

        addTo(levels,8,
                new OperationLimits(ADDITION, 12,35,12,35),
                new OperationLimits(SUBTRACTION, 30, 70, 15,39),
                new OperationLimits(MULTIPLICATION, 3,12,5,12),
                new OperationLimits(DIVISION, 2,12,2,12));

        addTo(levels,9,
                new OperationLimits(ADDITION, 500,1500,300,1450),
                new OperationLimits(SUBTRACTION, 550, 1200, 203,500),
                new OperationLimits(MULTIPLICATION, 15,30,12,20),
                new OperationLimits(DIVISION, 20,40,5,15));

        addTo(levels,10,
                new OperationLimits(ADDITION, 10_000,20_000,8_000,18_000),
                new OperationLimits(SUBTRACTION, 10_000, 15_000, 3000,9000),
                new OperationLimits(MULTIPLICATION, 25,50,15,35),
                new OperationLimits(DIVISION, 25,50,25,99));

        return levels;
    }


    public static void createAndAddLevelsOLD(){
        GameLevel level6 = new GameLevel(6);
        addTo(level6, ADDITION, 8,40,10,50);
        addTo(level6, SUBTRACTION, 40,80,21,61);
        addTo(level6, MULTIPLICATION, 2,12,12,15);
        addTo(level6, DIVISION, 2,15,7,15);

        GameLevel level7 = new GameLevel(7);
        addTo(level7, ADDITION, 100,150,80,170);
        addTo(level7, SUBTRACTION, 100,150,38,121);
        addTo(level7, MULTIPLICATION, 5,15,12,20);
        addTo(level7, DIVISION, 5,12,10,20);

        GameLevel level8 = new GameLevel(8);
        addTo(level8, ADDITION, 150,250,125,300);
        addTo(level8, SUBTRACTION, 200,400,130,301);
        addTo(level8, MULTIPLICATION, 2,12,2,12);
        addTo(level8, DIVISION, 8,15,2,40);
    }


    private static void addTo(Map<Integer, GameLevel> levels, int difficulty, OperationLimits ... operationLimitsArray){
        GameLevel gameLevel = new GameLevel(difficulty);
        for(OperationLimits operationLimits : operationLimitsArray){
            gameLevel.addOperationLimits(operationLimits);
        }
        if(levels.containsKey(difficulty)){
            throw new RuntimeException("Difficulty level already added!");
        }
        levels.put(difficulty, gameLevel);
    }


    private static void addTo(GameLevel gameLevel, MathOperation mathOperation, int number1Min,
                              int number1Max, int number2Min, int number2Max){
        gameLevel.addOperationLimits(mathOperation, new OperationLimits(number1Min, number1Max, number2Min, number2Max));
    }

}
