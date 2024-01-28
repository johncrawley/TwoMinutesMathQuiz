package com.jcrawley.tmmq.service.game.question;

public class QuestionCreatorForDivision extends QuestionCreator{

    public QuestionCreatorForDivision(){
        super(MathOperation.DIVISION);
    }

    @Override
    public MathQuestion createQuestion(){
        createParts();
        int largeNumber = part1 * part2;
        int dividedBy = part2;
        int answer = part1;
        String text = createQuestionText(largeNumber, dividedBy);
        return createFreshQuestion(text, answer);
    }

}
