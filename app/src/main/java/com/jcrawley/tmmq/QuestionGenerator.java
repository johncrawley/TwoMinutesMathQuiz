package com.jcrawley.tmmq;

import java.util.Random;

public class QuestionGenerator {


    private final Random random;

    public QuestionGenerator(){
          random = new Random(System.currentTimeMillis());
    }


    public MathQuestion generate(){
        int limit1 = 10;
        int limit2 = 10;
        int part1 = random.nextInt(limit1);
        int part2 = random.nextInt(limit2);
        int result = part1 + part2;
        String text = part1 + " + " + part2 + " = ";
        return  new MathQuestion(text, result);
    }


}
