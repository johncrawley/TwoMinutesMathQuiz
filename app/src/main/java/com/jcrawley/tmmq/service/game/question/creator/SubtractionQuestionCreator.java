package com.jcrawley.tmmq.service.game.question.creator;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

public class SubtractionQuestionCreator extends QuestionCreator {

    public SubtractionQuestionCreator(){
        super(MathOperation.SUBTRACTION);
    }


    @Override
    public MathQuestion createQuestion(){
        createParts();
        int subtraction = part1;
        int answer = part2;
        int largeNumber = subtraction + answer;
        String text = createQuestionText(largeNumber, subtraction);
        return createFreshQuestion(text, answer);
    }

}
