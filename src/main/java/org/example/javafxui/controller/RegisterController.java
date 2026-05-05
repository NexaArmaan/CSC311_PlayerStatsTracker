package org.example.javafxui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafxui.Session;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleRegister(ActionEvent event) throws Exception {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            messageLabel.setText("Please enter a valid email.");
            return;
        }

        if (password.length() < 4) {
            messageLabel.setText("Password must be at least 4 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        boolean success = Session.db.registerUser(username, email, password);

        if (success) {
            messageLabel.setText("Account created successfully.");
            goToLogin(event);
        } else {
            messageLabel.setText("Username or email already exists.");
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) throws Exception {
        loadScene(event, "/view/Login.fxml");
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