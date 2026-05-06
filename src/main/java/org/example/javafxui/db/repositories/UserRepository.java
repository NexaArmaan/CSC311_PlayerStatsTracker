package org.example.javafxui.db.repositories;

import org.example.javafxui.model.Stats;
import org.example.javafxui.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection conn;


    public UserRepository(Connection conn) {
        this.conn = conn;
    }
    // -----------------------------
    // USER REGISTRATION
    // -----------------------------
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


    // -----------------------------
    // USER LOGIN
    // -----------------------------
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
    // -----------------------------
    // UPDATE USER
    // -----------------------------
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

    // -----------------------------
    // DELETE USER
    // -----------------------------
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

    // -----------------------------
    // GET USER STATS (JOIN)
    // -----------------------------
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
}
