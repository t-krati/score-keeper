package com.krati.score_keeper.processor;

import com.krati.score_keeper.Constants;
import com.krati.score_keeper.dao.ScoreDao;
import com.krati.score_keeper.database.MaxHeapMapCache;
import com.krati.score_keeper.model.PlayerScore;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ScoreProcessor {

    private MaxHeapMapCache maxHeapMapCache;
    private ScoreDao scoreDao;
    Logger logger = Logger.getLogger("ScoreProcessor");

    public ScoreProcessor() {
    	logger.info("Creating ScoreProcessor instance...");
        maxHeapMapCache = MaxHeapMapCache.getInstance();
        scoreDao = new ScoreDao();
    }

    public void appendScore(HashMap<String, String> params) {
        PlayerScore score = new PlayerScore(params.get(Constants.playerName),
                Integer.parseInt(params.get(Constants.score)));
        logger.info("Appending score: " + score);
        scoreDao.appendScore(score);
        maxHeapMapCache.addScore(score);
    }

//    public List<PlayerScore> getTopKScores(int k) {
//    	logger.info("getting top " + k + "scores...");
//        return maxHeapMapCache.getTopKScores(k);
//    }
    
    public List<String> getTopKScores(int k) {
    	logger.info("getting top " + k + "scores...");
        return maxHeapMapCache.getTopKScores(k).stream().map(score -> score.toScoreString()).collect(Collectors.toList());
    }

    public List<String> getTopKScoresFromFile(int k) {
        try {
            maxHeapMapCache.clear();
            scoreDao.dumpCacheToFile();
            List<PlayerScore> scoreProcessorsList = scoreDao.getPlayerScoresFromFile();
            scoreProcessorsList.forEach(score -> maxHeapMapCache.addScore(score));
            logger.info("Getting top " + k + " scores from file...");
            // getTop5
            return maxHeapMapCache.getTopKScores(k).stream().map(score -> score.toScoreString()).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
            return Collections.emptyList();
        }
    }
}