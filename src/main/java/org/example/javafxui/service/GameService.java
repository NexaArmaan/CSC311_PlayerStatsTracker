package org.example.javafxui.service;

import org.example.javafxui.db.ConnDbOps;
import org.example.javafxui.Session;

import java.util.List;

public class GameService {
    private final ConnDbOps db;

    public GameService(ConnDbOps db) {
        this.db = db;
    }

    public void addGame(int userId, String gameName) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        if (gameName == null || gameName.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty.");
        }

        String trimmed = gameName.trim();

        List<String> existingGames = Session.game.getUserGames(userId);
        for (String game : existingGames) {
            if(game.equalsIgnoreCase(trimmed)) {
                throw new IllegalArgumentException("You already have a game with the same name '" + trimmed + "'.");
            }
        }

        boolean success = Session.game.insertGame(userId, gameName.trim());

        if (!success) {
            throw new RuntimeException("Could not save game.");
        }
    }
}