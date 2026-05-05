package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.javafxui.Session;

public class ReportController {

    @FXML
    private Label totalGamesLabel;

    @FXML
    private Label totalKillsLabel;

    @FXML
    private Label totalDeathsLabel;

    @FXML
    private Label totalAssistsLabel;

    @FXML
    private Label averageScoreLabel;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private Label kdRatioLabel;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            totalGamesLabel.setText("0");
            totalKillsLabel.setText("0");
            totalDeathsLabel.setText("0");
            totalAssistsLabel.setText("0");
            averageScoreLabel.setText("0");
            bestScoreLabel.setText("0");
            kdRatioLabel.setText("0.00");
            return;
        }

        int userId = Session.currentUser.id;

        totalGamesLabel.setText(String.valueOf(Session.db.getTotalGames(userId)));
        totalKillsLabel.setText(String.valueOf(Session.db.getTotalKills(userId)));
        totalDeathsLabel.setText(String.valueOf(Session.db.getTotalDeaths(userId)));
        totalAssistsLabel.setText(String.valueOf(Session.db.getTotalAssists(userId)));
        averageScoreLabel.setText(String.format("%.2f", Session.db.getAverageScore(userId)));
        bestScoreLabel.setText(String.valueOf(Session.db.getBestScore(userId)));
        kdRatioLabel.setText(String.format("%.2f", Session.db.getKDRatio(userId)));
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