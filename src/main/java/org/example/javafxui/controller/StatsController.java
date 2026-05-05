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

            boolean success = Session.db.insertStats(
                    gameId, kills, deaths, assists, score
            );

            if (success) {
                messageLabel.setText("Stats saved!");
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

    @FXML
    public void backToDashboard(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
    }
}