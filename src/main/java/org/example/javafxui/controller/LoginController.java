package org.example.javafxui.controller;
import org.example.javafxui.Session;
import org.example.javafxui.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label messageLabel;

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Enter username and email");
            return;
        }

        if (!email.contains("@")) {
            messageLabel.setText("Enter a valid email");
            return;
        }

        boolean success = Session.db.insertUser(username, email);

        if (success) {
            messageLabel.setText("Registered successfully. You can now login.");
        } else {
            messageLabel.setText("Registration failed.");
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Enter username and email");
            return;
        }

        User user = Session.db.loginUser(username, email);

        if (user == null) {
            messageLabel.setText("Invalid login.");
            return;
        }

        Session.currentUser = user;

        Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}