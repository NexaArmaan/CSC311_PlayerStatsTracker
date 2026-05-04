package model;

public class Stats {
    private int gameId;
    private int wins;
    private int losses;
    private int kills;

    public Stats(int gameId, int wins, int losses, int kills) {
        this.gameId = gameId;
        this.wins = wins;
        this.losses = losses;
        this.kills = kills;
    }

    public int getGameId() { return gameId; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getKills() { return kills; }

    public void setGameId(int gameId) { this.gameId = gameId; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setKills(int kills) { this.kills = kills; }
}