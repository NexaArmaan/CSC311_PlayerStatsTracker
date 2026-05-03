package org.example.javafxui.controller;
import org.example.javafxui.Session;
import org.example.javafxui.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label messageLabel;

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Enter username and email");
            return;
        }

        boolean success = Session.db.insertUser(username, email);

        if (success) {
            messageLabel.setText("Registered!");
            Session.currentUser = new User(1, username, email);
        } else {
            messageLabel.setText("Error registering");
        }
    }
}