package com.jcrawley.tmmq.service.game.question.creator;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

public class MultiplicationQuestionCreator extends QuestionCreator{
    public MultiplicationQuestionCreator(){
        super(MathOperation.MULTIPLICATION, 8);
    }
}