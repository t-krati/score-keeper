package com.krati.score_keeper.controller;

import com.krati.score_keeper.processor.ScoreProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.logging.Logger;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private final Logger logger = Logger.getLogger("ScoreController");

    private ScoreProcessor scoreProcessor;

    public ScoreController() {
        scoreProcessor = new ScoreProcessor();
    }

    @PostMapping(value = "/append")
    public ResponseEntity<String> append(@RequestParam HashMap<String, String> params) {
        try {
            scoreProcessor.appendScore(params);
            return ResponseEntity.ok("Player score Successfully added");
        } catch (Exception e) {
            logger.severe("An exception occurred in appending: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/getTop5Scores")
    public ResponseEntity<String> getTop5Scores() {
        try {
            logger.info("Get Top 5 Scores API called");
            return ResponseEntity.ok(scoreProcessor.getTopKScores(5).toString());
        } catch (Exception e) {
        	logger.severe("An exception occurred: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/getTop5ScoresFromFile")
    public ResponseEntity<String> getTop5ScoresFromFile() {
        try {
            logger.info("Get Top 5 Scores from file API called");
            return ResponseEntity.ok(scoreProcessor.getTopKScoresFromFile(5).toString());
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}