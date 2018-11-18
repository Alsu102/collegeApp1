package sample.forms.MainForm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.Main;
import sample.MySimpleController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController extends MySimpleController {

    @FXML
    ComboBox comboBox;

    @FXML
    TextField loginField, passwordField;

    public void LoginClick(ActionEvent action) {
        String value = (String)comboBox.getValue();
        if(value == null)
            value = comboBox.promptTextProperty().get();
        Main.instance.Authorization(loginField.getText(), passwordField.getText(), value);
    }
}
