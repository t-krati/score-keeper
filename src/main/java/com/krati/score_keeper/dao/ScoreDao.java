package com.krati.score_keeper.dao;

import com.krati.score_keeper.Constants;
import com.krati.score_keeper.database.CacheDatabase;
import com.krati.score_keeper.database.FileDatabase;
import com.krati.score_keeper.model.PlayerScore;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ScoreDao {

    private final FileDatabase fileDatabase;
    private final CacheDatabase cacheDatabase;
    Logger logger = Logger.getLogger("ScoreDao");


    public ScoreDao() {
    	logger.info("Creating ScoreDao instance...");
        fileDatabase = new FileDatabase();
        cacheDatabase = new CacheDatabase();
    }

    public List<PlayerScore> getPlayerScoresFromFile() throws FileNotFoundException {
    	logger.info("Inside getPlayerScoresFromFile method...");
        return fileDatabase.getAllScores().stream().map(PlayerScore::new).collect(Collectors.toList());
    }

    public void appendScore(PlayerScore score) {
    	cacheDatabase.append(score);
    	logger.info("Inside appendScore method...");
        if (cacheDatabase.getCacheLength() > Constants.cacheDumpLimit) {
            dumpCacheToFile();
        }
    }

    public void dumpCacheToFile() {
    	logger.info("Inside dumpCacheToFile method... ");
        fileDatabase.appendScores(cacheDatabase.getCache());
        cacheDatabase.clear();
    }

}