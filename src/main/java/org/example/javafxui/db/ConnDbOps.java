package org.example.javafxui.db;

import java.sql.*;


public class ConnDbOps {

    private Connection conn;

    public boolean connect() {
        try {
            conn = DriverManager.getConnection("jdbc:derby:PlayerStatsDB;create=true");
            createTables();
            System.out.println("Database connected.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean ensureConnected() {
        if (conn != null) {
            return true;
        }

        return connect();
    }

    private void createTables() {

        // USERS TABLE
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE USERS (" +
                            "user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "username VARCHAR(100) NOT NULL UNIQUE, " +
                            "email VARCHAR(150) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL" +
                            ")"
            );
            System.out.println("USERS table created.");
        } catch (SQLException ignored) {
            // Table probably already exists
        }

        // GAMES TABLE
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE GAMES (" +
                            "game_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "game_name VARCHAR(200) NOT NULL, " +
                            "FOREIGN KEY (user_id) REFERENCES USERS(user_id)" +
                            ")"
            );
            System.out.println("GAMES table created.");
        } catch (SQLException ignored) {
            // Table probably already exists
        }

        // STATS TABLE
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE STATS (" +
                            "stat_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "game_id INT NOT NULL, " +
                            "kills INT DEFAULT 0, " +
                            "deaths INT DEFAULT 0, " +
                            "assists INT DEFAULT 0, " +
                            "score INT DEFAULT 0, " +
                            "FOREIGN KEY (game_id) REFERENCES GAMES(game_id)" +
                            ")"
            );
            System.out.println("STATS table created.");
        } catch (SQLException ignored) {
            // Table probably already exists
        }
    }

    public Connection getConnection() {
        ensureConnected();
        return conn;
    }
}