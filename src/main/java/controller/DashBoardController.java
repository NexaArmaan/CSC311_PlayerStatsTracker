package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, " + Session.currentUser.username);
    }

    @FXML
    public void goToAddGame(ActionEvent e) throws Exception {
        load(e, "/org/example/demo/AddGame.fxml");
    }

    @FXML
    public void goToStats(ActionEvent e) throws Exception {
        load(e, "/org/example/demo/Stats.fxml");
    }

    private void load(ActionEvent e, String path) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}