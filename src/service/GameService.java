package service;

import db.DBConnection;
import model.Game;
import java.util.List;
public class GameService {

    public void addGame(int userId, String gameName) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        if (gameName == null || gameName.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty.");
        }

        DBConnection.addGame(userId, gameName.trim());
    }

    public List<Game> getGamesByUser(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        return DBConnection.getGamesByUser(userId);
    }

    public void addStats(int gameId, int wins, int losses, int kills) {
        validateStats(gameId, wins, losses, kills);
        DBConnection.addStats(gameId, wins, losses, kills);
    }

    public void updateStats(int gameId, int wins, int losses, int kills) {
        validateStats(gameId, wins, losses, kills);
        DBConnection.updateStats(gameId, wins, losses, kills);
    }

    public void deleteStats(int gameId) {
        if (gameId <= 0) {
            throw new IllegalArgumentException("Invalid game ID.");
        }

        DBConnection.deleteStats(gameId);
    }

    private void validateStats(int gameId, int wins, int losses, int kills) {
        if (gameId <= 0) throw new IllegalArgumentException("Invalid game ID.");
        if (wins < 0) throw new IllegalArgumentException("Wins cannot be negative.");
        if (losses < 0) throw new IllegalArgumentException("Losses cannot be negative.");
        if (kills < 0) throw new IllegalArgumentException("Kills cannot be negative.");
    }
}