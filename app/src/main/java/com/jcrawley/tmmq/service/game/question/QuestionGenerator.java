package com.jcrawley.tmmq.service.game.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {


    private final Random random;
    private final List<QuestionCreator> questionCreators;

    public QuestionGenerator(){

        random = new Random(System.currentTimeMillis());
        questionCreators = new ArrayList<>();
        questionCreators.add(new QuestionCreator("+",1,10, Integer::sum));
        questionCreators.add(new QuestionCreator("-",1,10, true, (x,y) -> x-y));
        questionCreators.add(new QuestionCreator("×",2,9, true, (x,y) -> x * y)); //TODO Large Number doesn't always need to be first for multiplication surely?
        questionCreators.add(new DivisionQuestionCreator(2,9));
    }


    public MathQuestion generate(){
        QuestionCreator questionCreator = getRandomQuestionCreator();
        return questionCreator.createQuestion();
    }



    private QuestionCreator getRandomQuestionCreator(){
        return questionCreators.get(random.nextInt(questionCreators.size()));
    }

}
