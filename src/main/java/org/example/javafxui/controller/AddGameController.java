package org.example.javafxui.controller;

import org.example.javafxui.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddGameController {

    @FXML private TextField gameNameField;
    @FXML private Label messageLabel;

    @FXML
    public void handleSaveGame() {
        String name = gameNameField.getText().trim();

        if (name.isEmpty()) {
            messageLabel.setText("Enter game name");
            return;
        }

        if (Session.currentUser == null) {
            messageLabel.setText("No user logged in");
            return;
        }

        try {
            Session.gameService.addGame(Session.currentUser.id, name);
            messageLabel.setText("Game added!");
            gameNameField.clear();
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}