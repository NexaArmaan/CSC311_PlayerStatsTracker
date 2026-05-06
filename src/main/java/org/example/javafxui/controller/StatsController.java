package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafxui.Session;

import java.util.List;

public class StatsController {

    @FXML
    private ComboBox<GameOption> gameComboBox;

    @FXML
    private TextField killsField;

    @FXML
    private TextField deathsField;

    @FXML
    private TextField assistsField;

    @FXML
    private TextField scoreField;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            messageLabel.setText("No user logged in.");
            return;
        }

        List<GameOption> games = Session.db.getUserGameOptions(Session.currentUser.id);
        gameComboBox.getItems().setAll(games);

        if (!games.isEmpty()) {
            gameComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleSaveStats() {
        GameOption selectedGame = gameComboBox.getValue();

        if (selectedGame == null) {
            messageLabel.setText("Please select a game first.");
            return;
        }

        try {
            int kills = Integer.parseInt(killsField.getText().trim());
            int deaths = Integer.parseInt(deathsField.getText().trim());
            int assists = Integer.parseInt(assistsField.getText().trim());
            int score = Integer.parseInt(scoreField.getText().trim());

            if (kills < 0 || deaths < 0 || assists < 0 || score < 0) {
                messageLabel.setText("Stats cannot be negative.");
                return;
            }

            boolean success = Session.db.insertStats(
                    selectedGame.getGameId(),
                    kills,
                    deaths,
                    assists,
                    score
            );

            if (success) {
                messageLabel.setText("Stats saved for " + selectedGame.getGameName() + "!");
                killsField.clear();
                deathsField.clear();
                assistsField.clear();
                scoreField.clear();
            } else {
                messageLabel.setText("Error saving stats.");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Enter valid numbers only.");
        } catch (Exception e) {
            messageLabel.setText("Stats could not be saved.");
            e.printStackTrace();
        }
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

    public static class GameOption {
        private final int gameId;
        private final String gameName;

        public GameOption(int gameId, String gameName) {
            this.gameId = gameId;
            this.gameName = gameName;
        }

        public int getGameId() {
            return gameId;
        }

        public String getGameName() {
            return gameName;
        }

        @Override
        public String toString() {
            return "ID " + gameId + " - " + gameName;
        }
    }
}