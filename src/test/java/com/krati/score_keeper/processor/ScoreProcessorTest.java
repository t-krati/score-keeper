package com.krati.score_keeper.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krati.score_keeper.dao.ScoreDao;
import com.krati.score_keeper.database.MaxHeapMapCache;
import com.krati.score_keeper.model.PlayerScore;

public class ScoreProcessorTest {

    @Mock
    private MaxHeapMapCache maxHeapMapCache;

    @Mock
    private ScoreDao scoreDao;

    @InjectMocks
    private ScoreProcessor scoreProcessor;

    @BeforeEach
    private void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAppendScoreSuccessful() {
        scoreProcessor.appendScore(getScore("A", "10"));
        verify(scoreDao, times(1)).appendScore(any());
        verify(maxHeapMapCache, times(1)).addScore(any());
    }

    @Test
    public void testGetTop5ScoresSuccessful() {
        scoreProcessor.getTopKScores(5);
        verify(scoreDao, times(0)).appendScore(any());
        verify(maxHeapMapCache, times(1)).getTopKScores(5);
    }

    @Test
    public void testGetTop5ScoresFromFileSuccessful() throws FileNotFoundException {
        when(scoreDao.getPlayerScoresFromFile()).thenReturn(Arrays.asList(new PlayerScore("A", 10)));
        doNothing().when(maxHeapMapCache).addScore(any());
        scoreProcessor.getTopKScoresFromFile(5);
        verify(scoreDao, times(1)).dumpCacheToFile();
        verify(maxHeapMapCache, times(1)).clear();
        verify(maxHeapMapCache, times(1)).getTopKScores(5);
    }

    @Test
    public void testGetTop5ScoresFromFileFileNotFoundException() throws FileNotFoundException{
        doThrow(FileNotFoundException.class).when(scoreDao).getPlayerScoresFromFile();
        doNothing().when(maxHeapMapCache).addScore(any());
        scoreProcessor.getTopKScoresFromFile(5);
        verify(scoreDao, times(1)).dumpCacheToFile();
        verify(maxHeapMapCache, times(1)).clear();
        verify(maxHeapMapCache, times(0)).getTopKScores(5);
    }

    private HashMap<String, String> getScore(String name, String score) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("score", score);
        return params;
    }

}
