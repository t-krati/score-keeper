package com.krati.score_keeper.database;


import com.krati.score_keeper.model.PlayerScore;

import java.util.ArrayList;
import java.util.List;

public class CacheDatabase {

    private List<PlayerScore> cache;

    public CacheDatabase() {
        cache = new ArrayList<>();
    }

    public void clear() {
        cache.clear();
    }

    public void append(PlayerScore score) {
        cache.add(score);
    }

    public List<PlayerScore> getCache() {
        return cache;
    }

    public int getCacheLength() {
        return cache.size();
    }


}