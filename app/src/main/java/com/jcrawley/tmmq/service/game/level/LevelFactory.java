package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.MathOperation;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    public static Map<Integer, GameLevel> createAndAddLevels(){

        Map<Integer, GameLevel> levels = new HashMap<>(10);
        GameLevel gameLevel1 = new GameLevel();
        addOperationLimitsTo(gameLevel1, MathOperation.ADDITION, 1,5,1,5);
        levels.put(1, gameLevel1);

        GameLevel gameLevel2 = new GameLevel();
        addOperationLimitsTo(gameLevel2, MathOperation.ADDITION, 1,10,1,10);
        levels.put(2, gameLevel2);

        GameLevel gameLevel3 = new GameLevel();
        addOperationLimitsTo(gameLevel3, MathOperation.ADDITION, 3,12,2,12);
        addOperationLimitsTo(gameLevel3, MathOperation.SUBTRACTION, 1,10,1,10);
        levels.put(3, gameLevel3);

        GameLevel gameLevel4 = new GameLevel();
        addOperationLimitsTo(gameLevel4, MathOperation.ADDITION, 4,15,4,15);
        addOperationLimitsTo(gameLevel4, MathOperation.SUBTRACTION, 1,12,1,12);
        levels.put(4, gameLevel4);

        GameLevel gameLevel5 = new GameLevel();
        addOperationLimitsTo(gameLevel5, MathOperation.ADDITION, 5,20,5,20);
        addOperationLimitsTo(gameLevel5, MathOperation.SUBTRACTION, 1,12,1,12);
        addOperationLimitsTo(gameLevel5, MathOperation.MULTIPLICATION, 2,5,2,5);
        levels.put(5, gameLevel5);

        GameLevel gameLevel6 = new GameLevel();
        addOperationLimitsTo(gameLevel6, MathOperation.ADDITION, 5,20,5,20);
        addOperationLimitsTo(gameLevel6, MathOperation.SUBTRACTION, 1,12,1,12);
        addOperationLimitsTo(gameLevel6, MathOperation.MULTIPLICATION, 2,10,2,10);
        addOperationLimitsTo(gameLevel6, MathOperation.DIVISION, 2,10,2,10);
        levels.put(6, gameLevel6);

        GameLevel gameLevel7 = new GameLevel();
        addOperationLimitsTo(gameLevel7, MathOperation.ADDITION, 5,25,5,25);
        addOperationLimitsTo(gameLevel7, MathOperation.SUBTRACTION, 1,15,1,15);
        addOperationLimitsTo(gameLevel7, MathOperation.MULTIPLICATION, 2,12,2,12);
        addOperationLimitsTo(gameLevel7, MathOperation.DIVISION, 4,12,2,12);
        levels.put(7, gameLevel7);

        return levels;
    }

    private static void addOperationLimitsTo(GameLevel gameLevel, MathOperation mathOperation, int number1Min,
                                      int number1Max, int number2Min, int number2Max){
        gameLevel.addOperationLimits(mathOperation, new OperationLimits(number1Min, number1Max, number2Min, number2Max));
    }

}
