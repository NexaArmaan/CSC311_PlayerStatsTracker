package org.example.javafxui.db;

import org.example.javafxui.model.User;

import java.util.ArrayList;
import java.util.List;

public class ConnDbOps {

    public boolean connect() {
        // TODO: Replace with real Derby connection
        System.out.println("Database connected placeholder");
        return true;
    }

    public boolean insertUser(String username, String email) {
        // TODO: Replace with Derby INSERT
        System.out.println("Inserted user placeholder: " + username + ", " + email);
        return true;
    }

    public User loginUser(String username, String email) {
        // TODO: Replace with Derby SELECT
        if (username == null || username.isBlank() || email == null || email.isBlank()) {
            return null;
        }

        // Temporary fake user so login flow works
        return new User(1, username, email);
    }

    public boolean insertGame(int userId, String gameName) {
        // TODO: Replace with Derby INSERT into games table
        System.out.println("Inserted game placeholder: " + gameName + " for user " + userId);
        return true;
    }

    public boolean insertStats(int gameId, int kills, int deaths, int assists, int score) {
        // TODO: Replace with Derby INSERT into stats table
        System.out.println("Inserted stats placeholder");
        return true;
    }

    public List<String> getUserGames(int userId) {
        // TODO: Replace with Derby SELECT
        List<String> games = new ArrayList<>();
        games.add("Valorant");
        games.add("Fortnite");
        games.add("Call of Duty");
        return games;
    }

    public int getTotalGames(int userId) {
        // TODO: Replace with Derby COUNT query
        return 3;
    }

    public int getTotalKills(int userId) {
        // TODO: Replace with Derby SUM query
        return 52;
    }

    public double getAverageScore(int userId) {
        // TODO: Replace with Derby AVG query
        return 1200.5;
    }
}