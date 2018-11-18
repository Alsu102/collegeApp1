package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MySimpleController implements Initializable {
    @FXML
    public Label statusField;

    public void setStatus(String message) {
        statusField.setText(message);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }

}
