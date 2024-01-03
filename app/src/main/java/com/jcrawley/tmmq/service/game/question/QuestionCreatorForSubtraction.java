package com.jcrawley.tmmq.service.game.question;

public class QuestionCreatorForSubtraction extends QuestionCreator {

    public QuestionCreatorForSubtraction(){
        super(MathOperation.SUBTRACTION, "-", (x,y)-> x - y);
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
