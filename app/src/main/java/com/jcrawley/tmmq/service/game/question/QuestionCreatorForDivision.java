package com.jcrawley.tmmq.service.game.question;

public class QuestionCreatorForDivision extends QuestionCreator{

    public QuestionCreatorForDivision(){
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
