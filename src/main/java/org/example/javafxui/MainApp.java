package org.example.javafxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Session.db.connect();

        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/view/Login.fxml")),
                900,
                600
        );

        stage.setTitle("Player Stats Tracker");
        stage.setScene(scene);
        stage.show();
    }
}