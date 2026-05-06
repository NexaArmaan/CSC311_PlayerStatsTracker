package org.example.javafxui.service;

import org.example.javafxui.model.Stats;

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

    public static int totalKills(List<Stats> list) {
        return list.stream().mapToInt(Stats::getKills).sum();
    }

    public static int totalDeaths(List<Stats> list) {
        return list.stream().mapToInt(Stats::getDeaths).sum();
    }

    public static int totalAssists(List<Stats> list) {
        return list.stream().mapToInt(Stats::getAssists).sum();
    }

    public static int totalScore(List<Stats> list) {
        return list.stream().mapToInt(Stats::getScore).sum();
    }

    public static double averageScore(List<Stats> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        return (double) totalScore(list) / list.size();
    }

}
