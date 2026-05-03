import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnDbOps {

    private Connection conn;
    public boolean connect() {
        try {
            conn = DriverManager.getConnection("jdbc:derby:PlayerStatsDB;create=true");
            createTables();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createTables() {
        try (Statement st = conn.createStatement()) {

            // USERS TABLE
            st.executeUpdate(
                    "CREATE TABLE USERS (" +
                            "user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "username VARCHAR(100) NOT NULL UNIQUE, "
            );

        } catch (SQLException ignored) {}

        try (Statement st = conn.createStatement()) {

            // GAMES TABLE
            st.executeUpdate(
                    "CREATE TABLE GAMES (" +
                            "game_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "game_name VARCHAR(200) NOT NULL, " +
                            "FOREIGN KEY (user_id) REFERENCES USERS(user_id))"
            );

        } catch (SQLException ignored) {}

        try (Statement st = conn.createStatement()) {

            // STATS TABLE
            st.executeUpdate(
                    "CREATE TABLE STATS (" +
                            "game_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY , " +
                            "kills INT DEFAULT 0, " +
                            "loses INT DEFAULT 0, " +
                            "wins INT DEFAULT 0, " +
                            "FOREIGN KEY (game_id) REFERENCES GAMES(game_id))"
            );

        } catch (SQLException ignored) {}
    }

    public boolean insertUser(String username, String email) {
        String sql = "INSERT INTO USERS (username, email) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getUser(int userId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM USERS WHERE user_id = ?");
            ps.setInt(1, userId);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUser(int userId, String username) {
        String sql = "UPDATE USERS SET username = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM USERS WHERE user_id = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertGame(int userId, String gameName) {
        String sql = "INSERT INTO GAMES (user_id, game_name) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, gameName);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }

    public boolean updateGame(int gameId, String newName) {
        String sql = "UPDATE GAMES SET game_name = ? WHERE game_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, gameId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGame(int gameId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM GAMES WHERE game_id = ?")) {
            ps.setInt(1, gameId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean addStats(int gameId, int kills, int loses, int wins) {
        String sql = "INSERT INTO STATS (game_id, kills, loses, wins) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.setInt(2, kills);
            ps.setInt(3, loses);
            ps.setInt(4, wins);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getStatsForGame(int gameId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM STATS WHERE game_id = ?");
            ps.setInt(1, gameId);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateStats(int gameId, int kills, int loses, int wins) {
        String sql = "UPDATE STATS SET kills = ?, loses = ?, wins = ? WHERE game_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kills);
            ps.setInt(2, loses);
            ps.setInt(3, wins);
            ps.setInt(4, gameId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStats(int gameId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM STATS WHERE game_id = ?")) {
            ps.setInt(1, gameId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
