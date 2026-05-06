package org.example.javafxui.db;

import org.example.javafxui.model.Stats;
import org.example.javafxui.model.User;
import org.example.javafxui.controller.StatsController.GameOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // ---------------------------
    // USER / AUTH METHODS
    // ---------------------------

    public boolean registerUser(String username, String email, String password) {
        if (!ensureConnected()) {
            return false;
        }

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

    public User loginUser(String usernameOrEmail, String password) {
        if (!ensureConnected()) {
            return null;
        }

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

    public boolean updateUser(int userId, String newUsername) {
        String sql = "UPDATE USERS SET username = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM USERS WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Stats> getUserStats(int userId) {
        List<Stats> list = new ArrayList<>();
        String sql =
                "SELECT s.game_id, s.kills, s.deaths, s.assists, s.score " +
                        "FROM STATS s JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ? ORDER BY s.stat_id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Stats(
                        rs.getInt("game_id"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("assists"),
                        rs.getInt("score")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public String[] getGameDetails(int gameId) {
        String sql =
                "SELECT g.game_name, " +
                        "COALESCE(SUM(s.kills), 0) AS total_kills, " +
                        "COALESCE(SUM(s.deaths), 0) AS total_deaths, " +
                        "COALESCE(SUM(s.assists), 0) AS total_assists, " +
                        "COALESCE(SUM(s.score), 0) AS total_score " +
                        "FROM GAMES g " +
                        "LEFT JOIN STATS s ON g.game_id = s.game_id " +
                        "WHERE g.game_id = ? " +
                        "GROUP BY g.game_name";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String gameName = rs.getString("game_name");
                int kills = rs.getInt("total_kills");
                int deaths = rs.getInt("total_deaths");
                int assists = rs.getInt("total_assists");
                int score = rs.getInt("total_score");

                double kdRatio = deaths == 0 ? kills : (double) kills / deaths;

                return new String[]{
                        gameName,
                        String.valueOf(kills),
                        String.valueOf(deaths),
                        String.valueOf(assists),
                        String.valueOf(score),
                        String.format("%.2f", kdRatio)
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    // ---------------------------
    // GAME METHODS
    // ---------------------------

    public boolean insertGame(int userId, String gameName) {
        String sql = "INSERT INTO GAMES (user_id, game_name) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, gameName);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getUserGames(int userId) {
        List<String> games = new ArrayList<>();
        String sql = "SELECT game_name FROM GAMES WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                games.add(rs.getString("game_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public boolean deleteGameAndStats(int gameId) {
        try {
            conn.setAutoCommit(false);

            String deleteStatsSql = "DELETE FROM STATS WHERE game_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteStatsSql)) {
                ps.setInt(1, gameId);
                ps.executeUpdate();
            }

            String deleteGameSql = "DELETE FROM GAMES WHERE game_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteGameSql)) {
                ps.setInt(1, gameId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackError) {
                rollbackError.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateGameName(int gameId, String newName) {
        String sql = "UPDATE GAMES SET game_name = ? WHERE game_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, gameId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ---------------------------
    // STATS METHODS
    // ---------------------------

    public boolean insertStats(int gameId, int kills, int deaths, int assists, int score) {
        String sql = "INSERT INTO STATS (game_id, kills, deaths, assists, score) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.setInt(2, kills);
            ps.setInt(3, deaths);
            ps.setInt(4, assists);
            ps.setInt(5, score);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStats(int statId, int kills, int deaths, int assists, int score) {
        String sql = "UPDATE STATS SET kills = ?, deaths = ?, assists = ?, score = ? WHERE stat_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kills);
            ps.setInt(2, deaths);
            ps.setInt(3, assists);
            ps.setInt(4, score);
            ps.setInt(5, statId);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String[] getLatestStatsForGame(int gameId) {
        String sql =
                "SELECT stat_id, kills, deaths, assists, score " +
                        "FROM STATS " +
                        "WHERE game_id = ? " +
                        "ORDER BY stat_id DESC " +
                        "FETCH FIRST 1 ROW ONLY";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new String[]{
                        String.valueOf(rs.getInt("stat_id")),
                        String.valueOf(rs.getInt("kills")),
                        String.valueOf(rs.getInt("deaths")),
                        String.valueOf(rs.getInt("assists")),
                        String.valueOf(rs.getInt("score"))
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotalGames(int userId) {
        String sql = "SELECT COUNT(*) AS total_games FROM GAMES WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_games");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalKills(int userId) {
        String sql =
                "SELECT COALESCE(SUM(s.kills), 0) AS total_kills " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_kills");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<String> getUserGamesWithIds(int userId) {
        List<String> games = new ArrayList<>();

        String sql = "SELECT game_id, game_name FROM GAMES WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int gameId = rs.getInt("game_id");
                String gameName = rs.getString("game_name");

                games.add("ID " + gameId + " - " + gameName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public double getAverageScore(int userId) {
        String sql =
                "SELECT COALESCE(AVG(s.score), 0) AS average_score " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("average_score");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalDeaths(int userId) {
        String sql =
                "SELECT COALESCE(SUM(s.deaths), 0) AS total_deaths " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalAssists(int userId) {
        String sql =
                "SELECT COALESCE(SUM(s.assists), 0) AS total_assists " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_assists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getBestScore(int userId) {
        String sql =
                "SELECT COALESCE(MAX(s.score), 0) AS best_score " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("best_score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<String[]> getStatsChartData(int userId) {
        List<String[]> data = new ArrayList<>();

        String sql =
                "SELECT g.game_name, s.kills, s.deaths, s.assists, s.score " +
                        "FROM STATS s " +
                        "JOIN GAMES g ON s.game_id = g.game_id " +
                        "WHERE g.user_id = ? " +
                        "ORDER BY s.stat_id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new String[]{
                        rs.getString("game_name"),
                        String.valueOf(rs.getInt("kills")),
                        String.valueOf(rs.getInt("deaths")),
                        String.valueOf(rs.getInt("assists")),
                        String.valueOf(rs.getInt("score"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<GameOption> getUserGameOptions(int userId) {
        List<GameOption> games = new ArrayList<>();

        String sql = "SELECT game_id, game_name FROM GAMES WHERE user_id = ? ORDER BY game_id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                games.add(new GameOption(
                        rs.getInt("game_id"),
                        rs.getString("game_name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public double getKDRatio(int userId) {
        int kills = getTotalKills(userId);
        int deaths = getTotalDeaths(userId);

        if (deaths == 0) {
            return kills;
        }

        return (double) kills / deaths;
    }
}