package db;

import model.Game;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    public static void addGame(int userId, String name) {
        System.out.println("Game added: " + name + " for user " + userId);
    }

    public static List<Game> getGamesByUser(int userId) {
        System.out.println("Fetching games for user " + userId);
        return new ArrayList<>();
    }

    public static void addStats(int gameId, int wins, int losses, int kills) {
        System.out.println("Stats added for game " + gameId);
    }

    public static void updateStats(int gameId, int wins, int losses, int kills) {
        System.out.println("Stats updated for game " + gameId);
    }

    public static void deleteStats(int gameId) {
        System.out.println("Stats deleted for game " + gameId);
    }
}