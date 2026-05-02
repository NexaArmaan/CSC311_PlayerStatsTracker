import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StatsController {

    @FXML private TextField winsField;
    @FXML private TextField lossesField;
    @FXML private TextField killsField;
    @FXML private Label statusLabel;

    @FXML
    public void handleSave() {
        statusLabel.setText("Stats saved!");
    }
}