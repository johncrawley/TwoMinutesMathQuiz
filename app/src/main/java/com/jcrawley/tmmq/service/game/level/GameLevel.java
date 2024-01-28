package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.QuestionCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLevel {

    private final List<QuestionCreator> questionCreators;
    private final int difficulty;
    private final Random random;

    public GameLevel(int difficulty){
        this.difficulty = difficulty;
        questionCreators = new ArrayList<>();
        random = new Random(System.currentTimeMillis());
    }


    public QuestionCreator getRandomQuestionCreator(){
        return questionCreators.get(random.nextInt(questionCreators.size()));
    }


    public void addQuestionCreator(QuestionCreator questionCreator){
        questionCreators.add(questionCreator);
    }


    public String getDifficultyStr(){
        return String.valueOf(difficulty);
    }


}
