package com.krati.score_keeper.processor;

import com.krati.score_keeper.Constants;
import com.krati.score_keeper.dao.ScoreDao;
import com.krati.score_keeper.database.MaxHeapMapCache;
import com.krati.score_keeper.model.PlayerScore;

import java.util.HashMap;
import java.util.List;

public class ScoreProcessor {

    MaxHeapMapCache maxHeapMapCache;
    private ScoreDao scoreDao;
    
    public ScoreProcessor() {
    	maxHeapMapCache = MaxHeapMapCache.getInstance();
    	scoreDao = new ScoreDao();
    }


    public void appendScore(HashMap<String, String> params) {
    	PlayerScore score = new PlayerScore(params.get(Constants.playerName), Integer.parseInt(params.get(Constants.score)));
        scoreDao.appendScore(score);
        maxHeapMapCache.addScore(score);
    }

    public List<PlayerScore> getTop5Scores() {
        return maxHeapMapCache.getTopKScores(5);
    }

    public List<PlayerScore> getTop5ScoresFromFile() {
        maxHeapMapCache = MaxHeapMapCache.getInstance();
        maxHeapMapCache.clear();
        scoreDao.dumpCacheToFile();
        List<PlayerScore> scoreProcessorsList = scoreDao.getPlayerScoresFromFile();
        scoreProcessorsList.forEach(score -> maxHeapMapCache.addScore(score));
        //getTop5
        return maxHeapMapCache.getTopKScores(5);
    }
}