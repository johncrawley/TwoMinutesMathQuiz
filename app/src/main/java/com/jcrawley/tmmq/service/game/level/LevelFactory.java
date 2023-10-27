package com.jcrawley.tmmq.service.game.level;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    private final Map<Integer,Level> levels;


    public LevelFactory(){
        levels = new HashMap<>(10);
        createAndAddLevels();
    }


    private void createAndAddLevels(){
        Level level1 = new Level();
        level1.enableAddition();
        level1.setFirstNumberLimits(1,5);
        level1.setSecondNumberLimits(1, 5);
        levels.put(1, level1);

        Level level2 = new Level();
        level2.enableAddition();
        level2.setFirstNumberLimits(1,10);
        level2.setSecondNumberLimits(1, 10);
        levels.put(2, level2);

        Level level3 = new Level();
        level3.enableAddition();
        level3.enableSubtraction();
        level3.setFirstNumberLimits(1,10);
        level3.setSecondNumberLimits(1, 10);
        levels.put(2, level2);
    }


    public Map<Integer, Level> getLevels(){
        return levels;
    }

}
