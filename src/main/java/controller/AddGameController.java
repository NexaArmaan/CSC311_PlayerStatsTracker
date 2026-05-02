import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddGameController {

    @FXML private TextField gameNameField;
    @FXML private Label statusLabel;

    @FXML
    public void handleSave() {
        String name = gameNameField.getText();

        if (name.isEmpty()) {
            statusLabel.setText("Enter game name");
            return;
        }

        // TEMP
        statusLabel.setText("Game added: " + name);
    }
}