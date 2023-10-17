package com.jcrawley.tmmq.service.question;

public class DivisionQuestionCreator extends QuestionCreator{

    public DivisionQuestionCreator(int minNumber, int maxNumber){
        super("÷", minNumber, maxNumber, (x,y)-> x / y);
    }

    @Override
    public MathQuestion createQuestion(){
        createParts();
        swapPartsIfLargeNumberShouldBeFirst();
        int newFirstPart = part1 * part2;
        int result = part1;
        String text = createQuestionText(newFirstPart, part2);
        return createFreshQuestion(text, result);
    }

}
