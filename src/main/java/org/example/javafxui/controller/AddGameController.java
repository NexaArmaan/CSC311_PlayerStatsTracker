package org.example.javafxui.controller;
import org.example.javafxui.Session;
import org.example.javafxui.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddGameController {

    @FXML private TextField gameNameField;
    @FXML private Label messageLabel;

    @FXML
    public void handleSaveGame() {

        String name = gameNameField.getText();

        if (name.isEmpty()) {
            messageLabel.setText("Enter game name");
            return;
        }

        boolean success = Session.db.insertGame(
                Session.currentUser.id,
                name
        );

        if (success) {
            messageLabel.setText("Game added!");
        } else {
            messageLabel.setText("Error adding game");
        }
    }
}