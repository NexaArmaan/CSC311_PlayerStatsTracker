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
    public void goToReport(ActionEvent e) throws Exception {
        load(e, "/view/Report.fxml");
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
}