package org.example.javafxui.db.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    private final Connection conn;

    public GameRepository(Connection conn) {
        this.conn = conn;
    }

    // -----------------------------
    // INSERT NEW GAME
    // -----------------------------
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

    // -----------------------------
    // GET ALL GAMES FOR USER
    // -----------------------------
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

    // -----------------------------
    // GET GAMES WITH IDs (for dropdowns)
    // -----------------------------
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

    // -----------------------------
    // GET GAME DETAILS
    // -----------------------------
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

    // -----------------------------
    // UPDATE GAME NAME
    // -----------------------------
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

    // -----------------------------
    // DELETE GAME + ITS STATS
    // -----------------------------
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
}