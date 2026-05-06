package org.example.javafxui.db.repositories;

import org.example.javafxui.controller.StatsController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsRepository {

    private final Connection conn;

    public AnalyticsRepository(Connection conn) {
        this.conn = conn;
    }



    // -----------------------------
    // CHART DATA FOR STATS
    // (returns kills, deaths, assists, score)
    // -----------------------------
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

    // -----------------------------
    // GAME OPTIONS FOR DROPDOWNS
    // -----------------------------
    public List<StatsController.GameOption> getUserGameOptions(int userId) {
        List<StatsController.GameOption> games = new ArrayList<>();

        String sql = "SELECT game_id, game_name FROM GAMES WHERE user_id = ? ORDER BY game_id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                games.add(new StatsController.GameOption(
                        rs.getInt("game_id"),
                        rs.getString("game_name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }
}

