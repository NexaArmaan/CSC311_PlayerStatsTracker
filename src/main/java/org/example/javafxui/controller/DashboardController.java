package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.javafxui.Session;
import javafx.scene.control.TextField;

import java.util.Optional;

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
    private Label selectedGameLabel;

    @FXML
    private Label selectedKillsLabel;

    @FXML
    private Label selectedDeathsLabel;

    @FXML
    private Label selectedAssistsLabel;

    @FXML
    private Label selectedScoreLabel;

    @FXML
    private TextField editGameNameField;

    @FXML
    private Label selectedKdLabel;

    private int selectedGameId = -1;

    @FXML
    public void initialize() {
        if (Session.currentUser == null) {
            welcomeLabel.setText("Welcome");
            totalGamesLabel.setText("0");
            totalKillsLabel.setText("0");
            averageScoreLabel.setText("0");
            return;
        }

        refreshDashboard();

        gamesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameId = extractGameId(newValue);
                showGameDetails(selectedGameId);
            }
        });
    }

    private void refreshDashboard() {
        int userId = Session.currentUser.id;

        welcomeLabel.setText("Welcome, " + Session.currentUser.username);
        totalGamesLabel.setText(String.valueOf(Session.db.getTotalGames(userId)));
        totalKillsLabel.setText(String.valueOf(Session.db.getTotalKills(userId)));
        averageScoreLabel.setText(String.format("%.1f", Session.db.getAverageScore(userId)));

        gamesListView.getItems().setAll(Session.db.getUserGamesWithIds(userId));

        clearGameDetails();
    }

    private int extractGameId(String gameText) {
        try {
            // Expected format: "ID 1 - Valorant"
            String[] parts = gameText.split(" ");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
    }

    private void showGameDetails(int gameId) {
        if (gameId <= 0) {
            clearGameDetails();
            return;
        }

        String[] details = Session.db.getGameDetails(gameId);

        if (details == null) {
            clearGameDetails();
            return;
        }

        selectedGameLabel.setText(details[0]);
        editGameNameField.setText(details[0]);
        selectedKillsLabel.setText(details[1]);
        selectedDeathsLabel.setText(details[2]);
        selectedAssistsLabel.setText(details[3]);
        selectedScoreLabel.setText(details[4]);
        selectedKdLabel.setText(details[5]);
    }

    private void clearGameDetails() {
        selectedGameId = -1;
        selectedGameLabel.setText("No game selected");
        editGameNameField.clear();
        selectedKillsLabel.setText("0");
        selectedDeathsLabel.setText("0");
        selectedAssistsLabel.setText("0");
        selectedScoreLabel.setText("0");
        selectedKdLabel.setText("0.00");
    }

    @FXML
    public void deleteSelectedGame() {
        if (selectedGameId <= 0) {
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game before deleting.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Game");
        confirmAlert.setHeaderText("Are you sure you want to delete this game?");
        confirmAlert.setContentText("This will also delete all stats connected to this game.");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = Session.db.deleteGameAndStats(selectedGameId);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Game Deleted", "The selected game was deleted successfully.");
                refreshDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Failed", "Could not delete the selected game.");
            }
        }
    }

    @FXML
    public void editSelectedGame() {
        if (selectedGameId <= 0) {
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game before editing.");
            return;
        }

        String newName = editGameNameField.getText().trim();

        if (newName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Name", "Game name cannot be empty.");
            return;
        }

        boolean success = Session.db.updateGameName(selectedGameId, newName);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Game Updated", "Game name updated successfully.");
            refreshDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Could not update the game name.");
        }
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
}