package org.example.javafxui.db.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRepository {

    private final Connection conn;

    public StatsRepository(Connection conn) {
        this.conn = conn;
    }

    // -----------------------------
    // INSERT NEW STATS ENTRY
    // -----------------------------
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
    // -----------------------------
    // DELETE STATS
    // -----------------------------
    public boolean deleteStats(int statId) {
        String sql = "DELETE FROM STATS WHERE stat_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, statId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -----------------------------
    // UPDATE EXISTING STATS ENTRY
    // -----------------------------
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

    // -----------------------------
    // GET LATEST STATS FOR A GAME
    // -----------------------------
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
    // -----------------------------
    // TOTAL GAMES PLAYED
    // -----------------------------
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


    // -----------------------------
    // TOTAL KILLS
    // -----------------------------
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
    // -----------------------------
    // TOTAL DEATHS
    // -----------------------------
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
    // -----------------------------
    // TOTAL ASSISTS
    // -----------------------------
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
    // -----------------------------
    // AVERAGE SCORE
    // -----------------------------
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

    // -----------------------------
    // BEST SCORE
    // -----------------------------
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

    // -----------------------------
    // GET K/D RATIO
    // -----------------------------

    public double getKDRatio(int userId) {
        int kills = getTotalKills(userId);
        int deaths = getTotalDeaths(userId);

        if (deaths == 0) {
            return kills;
        }

        return (double) kills / deaths;
    }
}
