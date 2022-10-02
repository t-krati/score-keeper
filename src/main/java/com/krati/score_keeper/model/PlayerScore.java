package com.krati.score_keeper.model;

import java.time.LocalDateTime;

public class PlayerScore implements Comparable<PlayerScore> {
    private final String playerName;
    private final int score;
    private final String timeStamp;

    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timeStamp = LocalDateTime.now().toString();
    }

    public PlayerScore(String str) {
        String[] list = str.split(",");
        this.playerName = list[0];
        this.score = Integer.parseInt(list[1]);
        this.timeStamp = list[2];
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getScore() {
        return this.score;
    }


    @Override
    public String toString() {
        return playerName + "," + score;
    }

    @Override
    public int compareTo(PlayerScore score) {
        if (this.score == score.getScore()) {
            return LocalDateTime.parse(this.timeStamp).compareTo(LocalDateTime.parse(this.timeStamp));
        }
        return this.score > score.getScore() ? 1 : -1;
    }
}