import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent e) throws Exception {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            errorLabel.setText("Enter username");
            return;
        }

        // TEMP MOCK (replace later)
        User user = new User(1, username);

        Session.currentUser = user;

        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/Dashboard.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}