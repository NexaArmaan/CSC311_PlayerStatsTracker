module org.example.javafxui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.javafxui to javafx.fxml;
    exports org.example.javafxui;
}