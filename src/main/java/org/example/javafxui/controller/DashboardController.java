package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private ListView<String> gamesListView;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            welcomeLabel.setText("Welcome");
            totalGamesLabel.setText("0");
            totalKillsLabel.setText("0");
            averageScoreLabel.setText("0");
            return;
        }

        int userId = Session.currentUser.id;

        welcomeLabel.setText("Welcome, " + Session.currentUser.username);
        totalGamesLabel.setText(String.valueOf(Session.db.getTotalGames(userId)));
        totalKillsLabel.setText(String.valueOf(Session.db.getTotalKills(userId)));
        averageScoreLabel.setText(String.format("%.1f", Session.db.getAverageScore(userId)));

        gamesListView.getItems().setAll(Session.db.getUserGamesWithIds(userId));
    }

    @FXML
    public void goToAddGame(ActionEvent event) throws Exception {
        loadScene(event, "/view/AddGame.fxml");
    }

    @FXML
    public void goToStats(ActionEvent event) throws Exception {
        loadScene(event, "/view/Stats.fxml");
    }

    @FXML
    public void goToReport(ActionEvent event) throws Exception {
        loadScene(event, "/view/Report.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws Exception {
        Session.currentUser = null;
        loadScene(event, "/view/Login.fxml");
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