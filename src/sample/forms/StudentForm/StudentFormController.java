package sample.forms.StudentForm;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;
import sample.Main;
import sample.MySimpleController;
import sample.tables.UserInfo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentFormController extends MySimpleController {

    @FXML
    TextField useridField, firstNameField, lastNameField, emailField, passwordField, phoneField, addressField, searchField;

    @FXML
    GridPane editProfile;

    @FXML
    HBox editProfileButtons;

    @FXML
    HBox tableFields;

    @FXML
    TableView<UserInfo> personTable;
    @FXML
    TableColumn<UserInfo, Integer> idCol;
    @FXML
    TableColumn<UserInfo, String> firstnameCol, lastnameCol, emailCol, phoneCol;

    private UserInfo selectedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedUser = Main.instance.userInfo;

        if(!Main.instance.userInfo.whoaim().equals("admin"))
            tableFields.setDisable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<UserInfo, Integer>("id"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("firstName"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("phone"));

        personTable.getItems().addAll(Main.instance.userInfo);

        personTable.setOnMouseClicked((MouseEvent event) -> {
            onSelectCell();
        });
    }

    private void onSelectCell() {
        if (personTable.getSelectionModel().getSelectedItem() != null)
            selectedUser = personTable.getSelectionModel().getSelectedItem();
    }

    public void clearFields(ActionEvent action) {
        useridField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        phoneField.clear();
        addressField.clear();
        editProfile.setDisable(true);
        editProfileButtons.setDisable(true);
    }

    public void saveFields(ActionEvent action) {
        int userid = Integer.parseInt(useridField.getText());
        boolean isOwner = Main.instance.isOwner(userid);
        UserInfo userInfo = selectedUser;

        if(!Main.instance.userInfo.whoaim().equals("admin"))
            userid = Main.instance.userInfo.getId();
        else
            userInfo = new UserInfo(userid,
                firstNameField.getText() != null ? firstNameField.getText() : "",
                lastNameField.getText() != null ? lastNameField.getText() : "",
                emailField.getText() != null ? emailField.getText() : "",
                Main.instance.userInfo.getGroupId(),
                phoneField.getText() != null ? phoneField.getText() : "",
                passwordField.getText() != null ? passwordField.getText() : "",
                addressField.getText() != null ? addressField.getText() : "");


        UserInfo user = Main.instance.saveUserInfo(selectedUser, userInfo);

        if(isOwner)
            Main.instance.userInfo = user;

        if(Main.instance.userInfo.whoaim().equals("admin"))
            selectedUser = userInfo;

        clearFields(action);
    }

    public void editInfo(ActionEvent event) {
        UserInfo user = Main.instance.userInfo;

        if(Main.instance.userInfo.whoaim().equals("admin"))
            user = selectedUser;

        useridField.setText(""+user.getId());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.clear();
        phoneField.setText(user.getPhone());
        addressField.setText(user.getAddress());
        editProfile.setDisable(false);
        editProfileButtons.setDisable(false);
    }

    public void findUser(ActionEvent action) {
        personTable.getItems().clear();
        List<UserInfo> personList = Main.instance.adminFindUser(searchField.getText());
        personTable.getItems().addAll(personList);
    }

    public void quit(ActionEvent ection) {
        Main.instance.close();
    }

}
