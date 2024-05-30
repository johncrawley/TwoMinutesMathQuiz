package com.jcrawley.tmmq.service.game.question.creator;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

public class MultiplicationQuestionCreator extends QuestionCreator{

    public MultiplicationQuestionCreator(){
        super(MathOperation.MULTIPLICATION);
    }

    @Override
    public MathQuestion createQuestion(){
        if(part1 + 10 < part2){
            swapParts();
        }
        return super.createQuestion();
    }


    private void swapParts(){
        int temp = part1;
        part1 = part2;
        part2 = temp;
    }
}