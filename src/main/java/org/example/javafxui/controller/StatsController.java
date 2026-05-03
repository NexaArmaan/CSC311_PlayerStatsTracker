package org.example.javafxui.controller;
import org.example.javafxui.Session;
import org.example.javafxui.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StatsController {

    @FXML private TextField gameIdField;
    @FXML private TextField killsField;
    @FXML private TextField deathsField;
    @FXML private TextField assistsField;
    @FXML private TextField scoreField;
    @FXML private Label messageLabel;

    @FXML
    public void handleSaveStats() {

        try {
            int gameId = Integer.parseInt(gameIdField.getText());
            int kills = Integer.parseInt(killsField.getText());
            int deaths = Integer.parseInt(deathsField.getText());
            int assists = Integer.parseInt(assistsField.getText());
            int score = Integer.parseInt(scoreField.getText());

            boolean success = Session.db.addStats(
                    gameId, kills, deaths, assists, score
            );

            if (success) {
                messageLabel.setText("Stats saved!");
            } else {
                messageLabel.setText("Error saving stats");
            }

        } catch (Exception e) {
            messageLabel.setText("Enter valid numbers");
        }
    }
}