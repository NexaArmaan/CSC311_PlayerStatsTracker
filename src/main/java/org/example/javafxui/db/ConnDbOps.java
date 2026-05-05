package org.example.javafxui.db;

import org.example.javafxui.model.User;

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

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE USERS (" +
                            "user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "username VARCHAR(100) NOT NULL UNIQUE, " +
                            "email VARCHAR(150) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL" +
                            ")"
            );
        } catch (SQLException ignored) {
            // Table probably already exists
        }
    }

    public boolean registerUser(String username, String email, String password) {
        String sql = "INSERT INTO USERS (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertGame(int userId, String gameName) {
        System.out.println("Inserted game: " + gameName + " for user " + userId);
        return true;
    }

    public User loginUser(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM USERS WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertStats(int gameId, int kills, int deaths, int assists, int score) {
        // Temporary placeholder until full Derby stats table is finished
        System.out.println("Inserted stats for game ID: " + gameId);
        System.out.println("Kills: " + kills);
        System.out.println("Deaths: " + deaths);
        System.out.println("Assists: " + assists);
        System.out.println("Score: " + score);
        return true;
    }

    public int getTotalGames(int userId) {
        return 0;
    }

    public int getTotalKills(int userId) {
        return 0;
    }

    public double getAverageScore(int userId) {
        return 0;
    }
}