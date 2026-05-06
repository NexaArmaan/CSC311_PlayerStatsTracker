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
import javafx.scene.control.Alert;
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
            showAlert(Alert.AlertType.INFORMATION, "Game Added", "Game added successfully.");
            gameNameField.clear();
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}