package com.krati.score_keeper.dao;

import com.krati.score_keeper.Constants;
import com.krati.score_keeper.database.CacheDatabase;
import com.krati.score_keeper.database.FileDatabase;
import com.krati.score_keeper.model.PlayerScore;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreDao {

    private final FileDatabase fileDatabase;
    private final CacheDatabase cacheDatabase;


    public ScoreDao() {
        fileDatabase = new FileDatabase();
        cacheDatabase = new CacheDatabase();
    }

    public List<PlayerScore> getPlayerScoresFromFile() {
        return fileDatabase.getAllScores().stream().map(PlayerScore::new).collect(Collectors.toList());
    }

    public void appendScore(PlayerScore score) {
    	cacheDatabase.append(score);
        if (cacheDatabase.getCacheLength() > Constants.cacheDumpLimit) {
            dumpCacheToFile();
        }
    }

    public void dumpCacheToFile() {
        fileDatabase.appendScores(cacheDatabase.getCache());
        cacheDatabase.clear();
    }

}