package org.example.javafxui.model;

public class Stats {
    private int gameId;
    private int kills;
    private int deaths;
    private int assists;
    private int score;

    public Stats(int gameId, int kills, int deaths, int assists, int score) {
        this.gameId = gameId;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.score = score;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}