package org.example.javafxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        if (!Session.db.connect()) {
            throw new RuntimeException("Database connection failed. App cannot start.");
        }

        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/view/Login.fxml")),
                1500,
                1000
        );

        stage.setTitle("Player Stats Tracker");
        stage.setScene(scene);
        stage.setWidth(1500);
        stage.setHeight(1000);
        stage.centerOnScreen();
        stage.show();
    }
}