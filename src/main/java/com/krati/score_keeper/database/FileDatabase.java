package com.krati.score_keeper.database;

import com.krati.score_keeper.Constants;
import com.krati.score_keeper.model.PlayerScore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileDatabase {
    private final Logger logger = Logger.getLogger("FileDatabase");

    public List<String> getAllScores() throws FileNotFoundException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(Constants.fileName))));
            List<String> scores = new ArrayList<>();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                scores.add(str);
            }
            bufferedReader.close();
            return scores;
        } catch (Exception e) {
            logger.severe("Error while reading file");
            throw new FileNotFoundException();
        }
    }

    public void appendScores(List<PlayerScore> scores) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(Constants.fileName),StandardOpenOption.CREATE,StandardOpenOption.APPEND)));
            scores.forEach(score -> {
                try {
                    bufferedWriter.append(score.toString());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            logger.info("Written to file successfully");
            bufferedWriter.close();
        } catch (Exception e) {
            logger.severe("Error while writing to file");
        }
    }

    public void appendScore(PlayerScore score) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(Constants.fileName),StandardOpenOption.CREATE, StandardOpenOption.APPEND)));
            bufferedWriter.append(score.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
            logger.info("Written to file successfully");
        } catch (Exception e) {
            logger.severe("Error while writing to file");
        }
    }


}