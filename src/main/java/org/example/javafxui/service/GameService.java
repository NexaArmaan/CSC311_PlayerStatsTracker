package org.example.javafxui.service;

import org.example.javafxui.db.ConnDbOps;

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

        boolean success = db.insertGame(userId, gameName.trim());

        if (!success) {
            throw new RuntimeException("Could not save game.");
        }
    }
}