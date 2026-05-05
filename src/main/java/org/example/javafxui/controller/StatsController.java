package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javafxui.Session;
import org.example.javafxui.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class StatsController {

    @FXML
    ComboBox<String> gameDropdown;
    //    @FXML private TextField gameIdField;
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
        if (Session.currentUser != null) {
            List<String> games = Session.db.getUserGamesWithIds(Session.currentUser.id);
            gameDropdown.getItems().addAll(games);
        }
    }

    @FXML
    public void handleSaveStats() {

        try {
            String selectedGame = gameDropdown.getValue();
            if (selectedGame == null) {
                messageLabel.setText("Pick a game");
                return;
            }
            int gameId = Integer.parseInt(selectedGame.split(" ")[1]);
            int kills = Integer.parseInt(killsField.getText());
            int deaths = Integer.parseInt(deathsField.getText());
            int assists = Integer.parseInt(assistsField.getText());
            int score = Integer.parseInt(scoreField.getText());

            boolean success = Session.db.insertStats(
                    gameId, kills, deaths, assists, score
            );

            if (success) {
                messageLabel.setText("Stats saved!");
                clearFields();
            } else {
                messageLabel.setText("Error saving stats");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Enter valid numbers only.");
        } catch (Exception e) {
            messageLabel.setText("Stats could not be saved. Make sure the Game ID exists.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        gameDropdown.setValue(null);
        killsField.clear();
        deathsField.clear();
        assistsField.clear();
        scoreField.clear();
    }

    @FXML
    public void backToDashboard(ActionEvent event) throws Exception {
        loadScene(event, "/view/Dashboard.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(
                getClass().getResource("/styles/app.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}