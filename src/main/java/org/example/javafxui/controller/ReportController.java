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

import java.util.List;

public class ReportController {

    @FXML
    private Label totalDeathsLabel;

    @FXML
    private Label totalAssistsLabel;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private Label kdRatioLabel;

    @FXML
    private BarChart<String, Number> kdaChart;

    @FXML
    private LineChart<String, Number> scoreChart;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            totalDeathsLabel.setText("0");
            totalAssistsLabel.setText("0");
            bestScoreLabel.setText("0");
            kdRatioLabel.setText("0.00");
            return;
        }

        int userId = Session.currentUser.id;

        totalDeathsLabel.setText(String.valueOf(Session.db.getTotalDeaths(userId)));
        totalAssistsLabel.setText(String.valueOf(Session.db.getTotalAssists(userId)));
        bestScoreLabel.setText(String.valueOf(Session.db.getBestScore(userId)));
        kdRatioLabel.setText(String.format("%.2f", Session.db.getKDRatio(userId)));

        loadKdaChart(userId);
        loadScoreChart(userId);
    }

    private void loadKdaChart(int userId) {
        kdaChart.getData().clear();
        kdaChart.setLegendVisible(true);
        kdaChart.setAnimated(false);

        XYChart.Series<String, Number> killsSeries = new XYChart.Series<>();
        killsSeries.setName("Kills");

        XYChart.Series<String, Number> deathsSeries = new XYChart.Series<>();
        deathsSeries.setName("Deaths");

        XYChart.Series<String, Number> assistsSeries = new XYChart.Series<>();
        assistsSeries.setName("Assists");

        List<String[]> rows = Session.db.getStatsChartData(userId);

        for (String[] row : rows) {
            String gameName = row[0];
            int kills = Integer.parseInt(row[1]);
            int deaths = Integer.parseInt(row[2]);
            int assists = Integer.parseInt(row[3]);

            killsSeries.getData().add(new XYChart.Data<>(gameName, kills));
            deathsSeries.getData().add(new XYChart.Data<>(gameName, deaths));
            assistsSeries.getData().add(new XYChart.Data<>(gameName, assists));
        }

        kdaChart.getData().addAll(killsSeries, deathsSeries, assistsSeries);
    }

    private void loadScoreChart(int userId) {
        scoreChart.getData().clear();
        scoreChart.setLegendVisible(false);
        scoreChart.setAnimated(false);

        XYChart.Series<String, Number> scoreSeries = new XYChart.Series<>();
        scoreSeries.setName("Score");

        List<String[]> rows = Session.db.getStatsChartData(userId);

        for (String[] row : rows) {
            String gameName = row[0];
            int score = Integer.parseInt(row[4]);

            scoreSeries.getData().add(new XYChart.Data<>(gameName, score));
        }

        scoreChart.getData().add(scoreSeries);
    }

    @FXML
    public void backToDashboard(ActionEvent event) throws Exception {
        loadScene(event, "/view/Dashboard.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/styles/app.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}