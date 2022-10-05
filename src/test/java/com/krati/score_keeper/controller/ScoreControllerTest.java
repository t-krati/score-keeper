package com.krati.score_keeper.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.krati.score_keeper.dao.ScoreDao;
import com.krati.score_keeper.model.PlayerScore;
import com.krati.score_keeper.processor.ScoreProcessor;

public class ScoreControllerTest {

    @InjectMocks
    private ScoreController scoreController;

    @Mock
    private ScoreDao scoreDao;

    @Mock
    private ScoreProcessor scoreProcessor;

    @BeforeEach
    private void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAppendScoreSuccessful() {
        ResponseEntity<String> response = scoreController.append(getScore("A", "10"));
        assertEquals("Player score Successfully added", response.getBody());
    }

    @Test
    public void testAppendScoreException() {
        doThrow(MockitoException.class).when(scoreProcessor).appendScore(any());
        ResponseEntity<String> response = scoreController.append(getScore("A", "10"));
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetTop5ScoresSuccessful() {
        when(scoreProcessor.getTopKScores(5)).thenReturn(Arrays.asList((new PlayerScore("A",10)).toScoreString()));
        ResponseEntity<String> response = scoreController.getTop5Scores();
        assertEquals("[A,10]", response.getBody());
    }

    @Test
    public void testGetTop5ScoresFromFileSuccessful() {
        when(scoreProcessor.getTopKScoresFromFile(5)).thenReturn(Arrays.asList((new PlayerScore("A",10)).toScoreString()));
        ResponseEntity<String> response = scoreController.getTop5ScoresFromFile();
        assertEquals("[A,10]", response.getBody());
    }

    private HashMap<String, String> getScore(String name, String score) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("score", score);
        return params;
    }

}
