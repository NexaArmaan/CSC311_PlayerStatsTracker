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
import javafx.stage.Stage;
import org.example.javafxui.Session;

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
    private BarChart<String, Number> kdaChart;

    @FXML
    private LineChart<String, Number> scoreLineChart;

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
        String[] games = {"Game 1", "Game 2", "Game 3", "Game 4", "Game 5"};
        int[][] kda = {{10, 4, 3}, {7, 6, 5}, {15, 2, 8}};
        int[] scores = {1200, 950, 700};

        XYChart.Series<String, Number> kills = new XYChart.Series<>();
        XYChart.Series<String, Number> deaths = new XYChart.Series<>();
        XYChart.Series<String, Number> assists = new XYChart.Series<>();

        kills.setName("Kills");
        deaths.setName("Deaths");
        assists.setName("Assists");

        for (int i = 0; i < games.length; i++) {
            kills.getData().add(new XYChart.Data<>(games[i], kda[i][0]));
            deaths.getData().add(new XYChart.Data<>(games[i], kda[i][1]));
            assists.getData().add(new XYChart.Data<>(games[i], kda[i][2]));
        }
        kdaChart.getData().clear();
        kdaChart.getData().addAll(kills, deaths, assists);

        XYChart.Series<String, Number> scoreSeries = new XYChart.Series<>();
        scoreSeries.setName("Score");
        for (int i = 0; i < games.length; i++) {
            scoreSeries.getData().add(new XYChart.Data<>(games[i], scores[i]));
        }
        scoreLineChart.getData().clear();
        scoreLineChart.getData().add(scoreSeries);

    }
}