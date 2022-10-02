package com.krati.score_keeper.database;

import com.krati.score_keeper.exceptions.database.CacheMemoryOverflowException;
import com.krati.score_keeper.exceptions.database.CacheMemoryUnderflowException;
import com.krati.score_keeper.model.PlayerScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MaxHeapMapCache {

    private static MaxHeapMapCache maxHeapMapCache = null;
    HashMap<String, Integer> positions;
    PlayerScore[] maxHeap;
    int max = 100000;
    int size;
    Logger logger = Logger.getLogger("HeapMapCache");
    private MaxHeapMapCache() {
        positions = new HashMap<>();
        maxHeap = new PlayerScore[max];
        size = 0;
    }

    public static MaxHeapMapCache getInstance() {
        if (maxHeapMapCache == null)
            maxHeapMapCache = new MaxHeapMapCache();
        return maxHeapMapCache;
    }

    public void clear() {
        positions.clear();
        size = 0;
    }

    public void addScore(PlayerScore score) {
        if (positions.containsKey(score.getPlayerName())) {
            int curPosition = positions.get(score.getPlayerName());
            PlayerScore newScore = new PlayerScore(score.getPlayerName(), score.getScore() + maxHeap[curPosition].getScore());
            maxHeap[curPosition] = newScore;
            rise(curPosition);
        } else {
            if (size == max) {
                throw new CacheMemoryOverflowException();
            }
            positions.put(score.getPlayerName(), size);
            maxHeap[size] = score;
            rise(size);
            size++;
        }
        logger.info("The updated contents are: \n");
        printMapContents();
    }

    public List<PlayerScore> getTopKScores(int K) {
        K = Math.min(K, size);
        List<PlayerScore> topKScores = new ArrayList<>();

        //Pop to 5 Scores
        for (int i = 0; i < K; i++) {
            topKScores.add(pop());
        }

        //Add them back to database
        topKScores.forEach(this::addScore);
        return topKScores;
    }


    private PlayerScore pop() {
        if (size == 0) {
            throw new CacheMemoryUnderflowException();
        }
        PlayerScore playerScore = maxHeap[0];
        positions.remove(playerScore.getPlayerName());
        if(size > 1) {
        	PlayerScore minPlayerScore = maxHeap[size - 1];
            positions.put(minPlayerScore.getPlayerName(), 0);
            maxHeap[0] = minPlayerScore;
        }
        size--;
        sink(0);
        return playerScore;
    }


    private void rise(int currentPosition) {
        if (currentPosition == 0)
            return;
        int parentPosition = (currentPosition - 1) >> 1;
        PlayerScore parentScore = maxHeap[parentPosition];
        PlayerScore currentScore = maxHeap[currentPosition];
        if (currentScore.compareTo(parentScore) > 0) {
            maxHeap[currentPosition] = parentScore;
            maxHeap[parentPosition] = currentScore;
            positions.put(currentScore.getPlayerName(), parentPosition);
            positions.put(parentScore.getPlayerName(), currentPosition);
            rise(parentPosition);
        }
    }

    private void sink(int currentPosition) {
    	if (2*currentPosition + 1 >= size)
            return;
        int leftChildPosition = (currentPosition << 1) + 1;
        int greaterChildPosition = leftChildPosition;
        
        PlayerScore currentScore = maxHeap[currentPosition];
        PlayerScore leftChildScore = maxHeap[leftChildPosition];
        if(leftChildPosition + 1 < size && leftChildScore.compareTo(maxHeap[leftChildPosition + 1]) < 0)
        	greaterChildPosition = leftChildPosition + 1;
   
        PlayerScore greaterChildScore = maxHeap[greaterChildPosition];
        if (currentScore.compareTo(greaterChildScore) < 0) {
            
        	maxHeap[currentPosition] = greaterChildScore;
            maxHeap[greaterChildPosition] = currentScore;
            positions.put(currentScore.getPlayerName(), greaterChildPosition);
            positions.put(greaterChildScore.getPlayerName(), currentPosition);
            sink(greaterChildPosition);
        }
    	
    }
    
    private void printMapContents() {
    	for(Map.Entry<String, Integer>e : positions.entrySet()) {
    		logger.info(e.getKey() + "" + maxHeap[e.getValue()].getScore() + ";");
    	}
    }


}