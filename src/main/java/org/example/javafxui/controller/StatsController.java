package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private int loadedStatId = -1;

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
                showAlert(Alert.AlertType.INFORMATION, "Stats Saved", "Stats saved successfully.");

                killsField.clear();
                deathsField.clear();
                assistsField.clear();
                scoreField.clear();
            } else {
                messageLabel.setText("Error saving stats.");
                showAlert(Alert.AlertType.ERROR, "Error", "Stats could not be saved.");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Enter valid numbers only.");
        } catch (Exception e) {
            messageLabel.setText("Stats could not be saved.");
            e.printStackTrace();
        }
    }

    @FXML
    public void loadExistingStats() {
        GameOption selectedGame = gameComboBox.getValue();

        if (selectedGame == null) {
            messageLabel.setText("Please select a game first.");
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game first.");
            return;
        }

        String[] stats = Session.db.getLatestStatsForGame(selectedGame.getGameId());

        if (stats == null) {
            loadedStatId = -1;
            messageLabel.setText("No stats found for this game yet.");
            showAlert(Alert.AlertType.INFORMATION, "No Stats Found", "This game does not have stats yet. Use Save New Stats first.");
            return;
        }

        loadedStatId = Integer.parseInt(stats[0]);

        killsField.setText(stats[1]);
        deathsField.setText(stats[2]);
        assistsField.setText(stats[3]);
        scoreField.setText(stats[4]);

        messageLabel.setText("Loaded latest stats for " + selectedGame.getGameName() + ".");
    }

    @FXML
    public void handleUpdateStats() {
        if (loadedStatId <= 0) {
            messageLabel.setText("Load existing stats before updating.");
            showAlert(Alert.AlertType.WARNING, "No Stats Loaded", "Please load existing stats before updating.");
            return;
        }

        try {
            int kills = Integer.parseInt(killsField.getText().trim());
            int deaths = Integer.parseInt(deathsField.getText().trim());
            int assists = Integer.parseInt(assistsField.getText().trim());
            int score = Integer.parseInt(scoreField.getText().trim());

            if (kills < 0 || deaths < 0 || assists < 0 || score < 0) {
                messageLabel.setText("Stats cannot be negative.");
                showAlert(Alert.AlertType.WARNING, "Invalid Stats", "Stats cannot be negative.");
                return;
            }

            boolean success = Session.db.updateStats(loadedStatId, kills, deaths, assists, score);

            if (success) {
                messageLabel.setText("Stats updated successfully.");
                showAlert(Alert.AlertType.INFORMATION, "Stats Updated", "Stats updated successfully.");
            } else {
                messageLabel.setText("Error updating stats.");
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Stats could not be updated.");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Enter valid numbers only.");
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter valid numbers only.");
        }
    }

    
    @FXML
    public void backToDashboard(ActionEvent event) throws Exception {
        loadScene(event, "/view/Dashboard.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 1500, 1000);
        scene.getStylesheets().add(
                getClass().getResource("/styles/app.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setWidth(1500);
        stage.setHeight(1000);
        stage.centerOnScreen();
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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