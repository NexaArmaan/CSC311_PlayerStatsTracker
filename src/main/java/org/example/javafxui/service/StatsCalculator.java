package org.example.javafxui.service;

import java.util.List;

public class StatsCalculator {

    public static double kdRatio(int kills, int deaths) {
        if (deaths == 0) {
            return kills;
        }
        return (double) kills / deaths;
    }

    public static double kdaRatio(int kills, int deaths, int assists) {
        if (deaths == 0) {
            return kills + assists;
        }
        return (double) (kills + assists) / deaths;
    }

    public static String kdFormat(int kills, int deaths) {
        return String.format("%.2f", kdRatio(kills, deaths));
    }

    public static int totalKills(List<?> list) {
        return 0;
    }

    public static int totalDeaths(List<?> list) {
        return 0;
    }

    public static int totalAssists(List<?> list) {
        return 0;
    }

    public static int totalScore(List<?> list) {
        return 0;
    }

    public static int averageScore(List<?> list) {
        return 0;
    }

}
