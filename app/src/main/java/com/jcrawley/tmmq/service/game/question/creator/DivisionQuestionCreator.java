package com.jcrawley.tmmq.service.game.question.creator;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

public class DivisionQuestionCreator extends QuestionCreator{

    public DivisionQuestionCreator(){
        super(MathOperation.DIVISION);
    }

    @Override
    public MathQuestion createQuestion(){
        createParts();
        int largeNumber = part1 * part2;
        int dividedBy = part1;
        int answer = part2;
        String text = createQuestionText(largeNumber, dividedBy);
        return createFreshQuestion(text, answer);
    }

}
