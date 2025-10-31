package com.jcrawley.tmmq.service.score.saver;

import com.jcrawley.tmmq.service.score.ScoreStatistics;

public interface ScoreSaver {

    void saveScores(ScoreStatistics stats);
}
