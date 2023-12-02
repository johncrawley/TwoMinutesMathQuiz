package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.MathOperation;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    public static Map<Integer, GameLevel> createAndAddLevels(){

        Map<Integer, GameLevel> levels = new HashMap<>(10);
        GameLevel gameLevel1 = new GameLevel(1);
        addTo(gameLevel1, MathOperation.ADDITION, 1,5,1,5);
        levels.put(1, gameLevel1);

        GameLevel gameLevel2 = new GameLevel(2);
        addTo(gameLevel2, MathOperation.ADDITION, 2,12,3,10);
        levels.put(2, gameLevel2);

        GameLevel level3 = new GameLevel(3);
        addTo(level3, MathOperation.ADDITION, 4,15,3,15);
        addTo(level3, MathOperation.SUBTRACTION, 1,12,4,15);
        levels.put(3, level3);

        GameLevel level4 = new GameLevel(4);
        addTo(level4, MathOperation.ADDITION, 5,20,5,20);
        addTo(level4, MathOperation.SUBTRACTION, 3,15,4,20);
        addTo(level4, MathOperation.MULTIPLICATION, 2,10,2,5);
        levels.put(4, level4);

        GameLevel level5 = new GameLevel(5);
        addTo(level5, MathOperation.ADDITION, 7,25,8,30);
        addTo(level5, MathOperation.SUBTRACTION, 7,30,10,35);
        addTo(level5, MathOperation.MULTIPLICATION, 3,12,5,12);
        addTo(level5, MathOperation.DIVISION, 2,12,5,12);
        levels.put(5, level5);

        GameLevel level6 = new GameLevel(6);
        addTo(level6, MathOperation.ADDITION, 8,40,10,50);
        addTo(level6, MathOperation.SUBTRACTION, 10,50,40,70);
        addTo(level6, MathOperation.MULTIPLICATION, 2,12,12,15);
        addTo(level6, MathOperation.DIVISION, 2,15,7,15);
        levels.put(6, level6);

        GameLevel level7 = new GameLevel(7);
        addTo(level7, MathOperation.ADDITION, 100,150,80,170);
        addTo(level7, MathOperation.SUBTRACTION, 100,150,80,170);
        addTo(level7, MathOperation.MULTIPLICATION, 5,15,12,20);
        addTo(level7, MathOperation.DIVISION, 5,12,10,20);
        levels.put(7, level7);

        GameLevel level8 = new GameLevel(8);
        addTo(level8, MathOperation.ADDITION, 150,250,125,300);
        addTo(level8, MathOperation.SUBTRACTION, 120,170,130,300);
        addTo(level8, MathOperation.MULTIPLICATION, 2,12,2,12);
        addTo(level8, MathOperation.DIVISION, 8,15,2,40);
        levels.put(8, level8);

        GameLevel level9 = new GameLevel(9);
        addTo(level9, MathOperation.ADDITION, 500,1000,700,2500);
        addTo(level9, MathOperation.SUBTRACTION, 500,1500,800,2000);
        addTo(level9, MathOperation.MULTIPLICATION, 15,30,15,40);
        addTo(level9, MathOperation.DIVISION, 12,20,2,70);
        levels.put(9, level9);


        GameLevel level10 = new GameLevel(10);
        addTo(level10, MathOperation.ADDITION, 10_000,20_000,12_111,25_123);
        addTo(level10, MathOperation.SUBTRACTION, 10_000,15_000,20_000,50_000);
        addTo(level10, MathOperation.MULTIPLICATION, 25,50,30,100);
        addTo(level10, MathOperation.DIVISION, 25,50,25,99);
        levels.put(10, level10);


        return levels;
    }

    private static void addTo(GameLevel gameLevel, MathOperation mathOperation, int number1Min,
                              int number1Max, int number2Min, int number2Max){
        gameLevel.addOperationLimits(mathOperation, new OperationLimits(number1Min, number1Max, number2Min, number2Max));
    }

}
