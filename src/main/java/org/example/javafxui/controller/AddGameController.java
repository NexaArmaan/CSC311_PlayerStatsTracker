package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javafxui.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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