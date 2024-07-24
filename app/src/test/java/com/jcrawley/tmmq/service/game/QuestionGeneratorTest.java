package com.jcrawley.tmmq.service.game;

import static org.junit.Assert.assertEquals;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.level.OperationLimits;
import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class QuestionGeneratorTest {

    private QuestionGenerator questionGenerator;

    @Before
    public void init(){
        questionGenerator = new QuestionGenerator();

    }


    @Test
    public void questionsWillNotRepeatUntilExhausted(){
        GameLevel gameLevel = new GameLevel(5);

        QuestionCreator questionCreator = new QuestionCreator(MathOperation.ADDITION);
        questionCreator.setOperationLimits(new OperationLimits(MathOperation.ADDITION, 1, 2,1,2));
        gameLevel.addQuestionCreator(questionCreator);

        // the possible questions should be 1+1, 1+2, 2+1, 2+2
        // so four values and should not repeat until all values have been tried
        int maxNumberOfUniqueQuestions = 4;
       // assertNumberOfUniqueQuestions(maxNumberOfUniqueQuestions, gameLevel);

        QuestionCreator questionCreator2 = new QuestionCreator(MathOperation.MULTIPLICATION);
        questionCreator2.setOperationLimits(new OperationLimits(MathOperation.MULTIPLICATION, 2, 4,2,4));
        gameLevel.addQuestionCreator(questionCreator2);
        // existing 4 +
        // 2 * 2
        // 2 * 3
        // 2 * 4
        // 3 * 2
        // 3 * 3
        // 3 * 4
        // 4 * 2
        // 4 * 3
        // 4 * 4
        int maxNumberOfUniqueQuestions2 = maxNumberOfUniqueQuestions + 9;
        assertNumberOfUniqueQuestions(maxNumberOfUniqueQuestions2, gameLevel);
    }


    private void assertNumberOfUniqueQuestions(int expectedMax, GameLevel gameLevel){
        Set<String> questions = new HashSet<>();
        for(int i = 0; i < expectedMax; i ++){
            MathQuestion mathQuestion = questionGenerator.generateRandomQuestionFrom(gameLevel);
            questions.add(mathQuestion.getQuestionText());
            System.out.println("questionText : " + mathQuestion.getQuestionText());
        }
        for(String questionText : questions){
            System.out.println(" question: " + questionText);
        }
        assertEquals(expectedMax, questions.size());
    }

}
