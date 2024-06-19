package com.jcrawley.tmmq.service.game.level;

import static org.junit.Assert.assertEquals;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.creator.MultiplicationQuestionCreator;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;

import org.junit.Before;
import org.junit.Test;

public class GameLevelTest {

    private GameLevel gameLevel;
    private int difficulty = 5;

    @Before
    public void init(){
        gameLevel = new GameLevel(difficulty);
    }


    @Test
    public void canAddQuestionCreator(){
        OperationLimits limits1 = new OperationLimits(MathOperation.MULTIPLICATION, 2,2,4,10);
        OperationLimits limits2 = new OperationLimits(MathOperation.MULTIPLICATION, 3,3,2,6);
        OperationLimits limits3 = new OperationLimits(MathOperation.MULTIPLICATION, 4,10,5,12);

        QuestionCreator qc1 = new MultiplicationQuestionCreator();
        qc1.setOperationLimits(limits1);

        QuestionCreator qc2 = new MultiplicationQuestionCreator();
        qc2.setOperationLimits(limits2);

        QuestionCreator qc3 = new MultiplicationQuestionCreator();
        qc3.setOperationLimits(limits3);

        gameLevel.addQuestionCreator(qc1);
        assertEquals(1, gameLevel.getLimitsRangeFor(MathOperation.MULTIPLICATION));

        gameLevel.addQuestionCreator(qc2);
        assertEquals(2, gameLevel.getLimitsRangeFor(MathOperation.MULTIPLICATION));

        gameLevel.addQuestionCreator(qc3);
        assertEquals(9, gameLevel.getLimitsRangeFor(MathOperation.MULTIPLICATION));
    }

}
