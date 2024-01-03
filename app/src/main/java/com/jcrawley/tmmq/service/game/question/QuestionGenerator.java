package com.jcrawley.tmmq.service.game.question;

import com.jcrawley.tmmq.service.game.level.GameLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {


    private final Random random;
    private final List<QuestionCreator> questionCreators, currentQuestionCreators;

    public QuestionGenerator(){
        random = new Random(System.currentTimeMillis());
        questionCreators = new ArrayList<>();
        questionCreators.add(new QuestionCreator(MathOperation.ADDITION, "+", Integer::sum));
       // questionCreators.add(new QuestionCreator(MathOperation.SUBTRACTION, "-", true, (x,y) -> x - y));
        questionCreators.add(new QuestionCreatorForSubtraction());
        questionCreators.add(new QuestionCreator(MathOperation.MULTIPLICATION, "×", (x,y) -> x * y));
        questionCreators.add(new QuestionCreatorForDivision());

        currentQuestionCreators = new ArrayList<>();
    }


    public void setGameLevel(GameLevel gameLevel){
        currentQuestionCreators.clear();
        questionCreators.forEach(qc -> qc.setGameLevel(gameLevel));
        for(QuestionCreator questionCreator : questionCreators){
            if(gameLevel.containsMathOperation(questionCreator.getMathOperation())){
                currentQuestionCreators.add(questionCreator);
            }
        }

    }


    public MathQuestion generate(){
        QuestionCreator questionCreator = getRandomQuestionCreator();
        return questionCreator.createQuestion();
    }


    private QuestionCreator getRandomQuestionCreator(){
        return currentQuestionCreators.get(random.nextInt(currentQuestionCreators.size()));
    }

}
