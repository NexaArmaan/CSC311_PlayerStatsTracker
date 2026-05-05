package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.javafxui.Session;
import org.example.javafxui.model.Stats;

import java.util.List;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalGamesLabel;

    @FXML
    private Label totalKillsLabel;

    @FXML
    private Label averageScoreLabel;

    @FXML
    private ListView<String> gamesListView;

    @FXML
    private BarChart<String, Number> kdaChart;

    @FXML
    private LineChart<String, Number> scoreChart;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            welcomeLabel.setText("Welcome");
            totalGamesLabel.setText("Total Games: 0");
            totalKillsLabel.setText("Total Kills: 0");
            averageScoreLabel.setText("Average Score: 0");
            return;
        }

        int userId = Session.currentUser.id;

        welcomeLabel.setText("Welcome, " + Session.currentUser.username);
        totalGamesLabel.setText("Total Games: " + Session.db.getTotalGames(userId));
        totalKillsLabel.setText("Total Kills: " + Session.db.getTotalKills(userId));
        averageScoreLabel.setText("Average Score: " + Session.db.getAverageScore(userId));

        gamesListView.getItems().setAll(Session.db.getUserGamesWithIds(userId));
        loadCharts();
    }

    @FXML
    public void goToAddGame(ActionEvent e) throws Exception {
        load(e, "/view/AddGame.fxml");
    }

    @FXML
    public void goToStats(ActionEvent e) throws Exception {
        load(e, "/view/Stats.fxml");
    }

    @FXML
    public void logout(ActionEvent e) throws Exception {
        Session.currentUser = null;
        load(e, "/view/Login.fxml");
    }

    private void load(ActionEvent e, String path) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void loadCharts() {
        List<Stats> allStats = Session.db.getUserStats(Session.currentUser.id);
        List<String> allLabels = Session.db.getUserGamesWithIds(Session.currentUser.id);

        final int MAX_GAMES = 10;
        int start = Math.max(0, allStats.size() - MAX_GAMES);

        List<Stats> stats = allStats.subList(start, allStats.size());
        List<String> gameLabels = allLabels.subList(Math.max(0, allLabels.size() - MAX_GAMES), allLabels.size());

        XYChart.Series<String, Number> kills = new XYChart.Series<>();
        XYChart.Series<String, Number> deaths = new XYChart.Series<>();
        XYChart.Series<String, Number> assists = new XYChart.Series<>();
        kills.setName("Kills");
        deaths.setName("Deaths");
        assists.setName("Assists");

        XYChart.Series<String, Number> scoreSeries = new XYChart.Series<>();
        scoreSeries.setName("Score");

        for (int i = 0; i < stats.size(); i++) {
            Stats stat = stats.get(i);
            String label;
            if (i < gameLabels.size()) {
                String full = gameLabels.get(i);
                int dashIndex = full.indexOf(" - ");
                label = (dashIndex != -1) ? full.substring(dashIndex + 3) : full;
            } else {
                label = "Game " + (i + 1);
            }
            kills.getData().add(new XYChart.Data<>(label, stat.getKills()));
            deaths.getData().add(new XYChart.Data<>(label, stat.getDeaths()));
            assists.getData().add(new XYChart.Data<>(label, stat.getAssists()));
            scoreSeries.getData().add(new XYChart.Data<>(label, stat.getScore()));
        }

        kdaChart.getData().clear();
        kdaChart.getData().addAll(kills, deaths, assists);

        scoreChart.getData().clear();
        scoreChart.getData().add(scoreSeries);
    }
}