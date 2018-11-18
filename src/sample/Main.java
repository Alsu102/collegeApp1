package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.tables.UserInfo;
import utils.SQLSystem;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements IHaveLog {

    public boolean isOwner(int userid) {
        if(userInfo.getId() == userid)
            return true;
        return false;
    }

    public void close() {
        Platform.exit();
    }

    enum ECurrentForm {
        MAIN, STUDENT, ADMIN
    }

    private String lastMessage = "";
    public static Main instance = new Main();

    private Stage mainStage;
    private MySimpleController mainController;

    private Stage studentStage;
    private MySimpleController studentController;

    private Stage adminStage;
    private MySimpleController adminController;

    public static SQLSystem sql;
    public UserInfo userInfo = null;
    public ECurrentForm currentForm = ECurrentForm.MAIN;

    public Main() {
        //
    }

    private final String AUTH_ERROR = "User with this username and password cannot be found!";
    private final String SAVE_OK = "Info has been saved!";
    private final String SAVE_FAILED = "ERROR!!! Cant be saved info!";

    @Override
    public void start(Stage primaryStage) throws Exception{
        sql = new SQLSystem(this, SQLSystem.EnumSQL.MySQL);
        sql.setParams("localhost", 3306, "cas", "root", "12345678");
        try {
            sql.connectSQL();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        instance = this;
        loadAuthorizationForm(primaryStage);
    }

    private void loadAuthorizationForm(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forms/MainForm/MainForm.fxml"));
        Parent root = loader.load();
        mainController = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setTitle("CAS");
        primaryStage.setScene(scene);
        primaryStage.show();
        this.mainStage = primaryStage;
        mainStage.setOnCloseRequest(e -> Platform.exit());
        mainController.setStatus(this.lastMessage);
    }

    private void loadStudentForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forms/StudentForm/StudentForm.fxml"));
        Parent root = loader.load();
        studentController = loader.getController();
        Scene scene = new Scene(root);
        studentStage = new Stage();
        studentStage.setTitle("CAS: Student Info");
        studentStage.setScene(scene);
        studentStage.show();
        studentStage.setOnCloseRequest(e -> Platform.exit());
        currentForm = ECurrentForm.STUDENT;
        studentController.setStatus(this.lastMessage);
    }

    private void loadAdminForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forms/StudentForm/StudentForm.fxml"));
        Parent root = loader.load();
        adminController = loader.getController();
        Scene scene = new Scene(root);
        adminStage = new Stage();
        adminStage.setTitle("CAS: Admin");
        adminStage.setScene(scene);
        adminStage.show();
        adminStage.setOnCloseRequest(e -> Platform.exit());
        currentForm = ECurrentForm.ADMIN;
    }

    public static void main(String[] args) {

        instance.sql = sql;

        launch(args);
    }

    public void Authorization(String login, String password, String selectedRole) {
        login = filterField(login);
        password = filterField(password);

        int group = UserInfo.getGroupId(selectedRole);

        if(login.length() >= 3 && password.length() >= 3)
        {
            int ID = 0;
            try {
                ID = Integer.parseInt(login);
            } catch (NumberFormatException ex){
            }
            String sql_query = "";
            if (ID > 0)
                sql_query = "SELECT * FROM users WHERE id=" + ID + " AND group_id=" + group + " LIMIT 1";
            else
                sql_query = "SELECT * FROM users WHERE email='" + login + "'" + " AND group_id=" + group + " LIMIT 1";

            ResultSet rs = this.sql.query(sql_query);
            try {
                while (rs != null && rs.next())
                {
                    String pass = rs.getString("password").trim();
                    if(pass.equals(password))
                    {
                        int id = rs.getInt("id");
                        String firstName = rs.getString("firstname").trim();
                        String lastName = rs.getString("lastname").trim();
                        String email = rs.getString("email").trim();
                        int group_id = rs.getInt("group_id");
                        String phone = rs.getString("phone");
                        String address = rs.getString("address");
                        userInfo = new UserInfo(id, firstName, lastName, email, group_id, phone, pass, address);
                    }
                }
                if(userInfo != null) {
                    //AUTH OK
                    mainStage.hide();
                    switch(userInfo.whoaim())
                    {
                        default:
                        case "guest":
                            break;
                        case "student":
                            if(studentStage == null)
                                loadStudentForm();
                            else
                                studentStage.show();
                            break;
                        case "admin":
                            if(adminStage == null)
                                loadAdminForm();
                            else
                                adminStage.show();
                            break;
                    }
                }
                else
                    errorMessage("Authorization", AUTH_ERROR);

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            System.out.println("Login and Password length can by more 2 symbols");
    }

    public String filterField(String field) {
        return !field.isEmpty() ? field.trim().replace(";", "").replace("'", "").replace( "\"", "" ) : "";
    }

    public List<UserInfo> getStudentList() throws SQLException {
        ResultSet rs = sql.query("select * from users where group_id=" + UserInfo.getGroupId("student"));
        List<UserInfo> personList = new ArrayList<UserInfo>();
        while (rs != null && rs.next())
        {
            int id = rs.getInt("id");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String email = rs.getString("email");
            int group_id = rs.getInt("group_id");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String pass = rs.getString("password");
            UserInfo person = new UserInfo(id, firstName, lastName, email, group_id, phone, pass, address);
            personList.add(person);
        }
        return personList;
    }

    public UserInfo saveUserInfo(int id, String firstName, String lastName, String email, int group_id, String password, String phone, String address) {
        firstName = filterField(firstName);
        lastName = filterField(lastName);
        email = filterField(email);
        password = filterField(password);
        phone = filterField(phone);
        address = filterField(address);
        UserInfo user = new UserInfo(id, firstName, lastName, email, group_id, phone, password, address);
        return saveUserInfo(user, this.userInfo);
    }

    public UserInfo saveUserInfo(UserInfo selectedUser, UserInfo user) {
        String result = "";
        String query = "";
        if(!user.getFirstName().equals(selectedUser.getFirstName()))
            query += query.isEmpty() ? " firstname='" + user.getFirstName() + "'" : ", firstname='" + user.getFirstName() + "'";
        if(!user.getLastName().equals(selectedUser.getLastName()))
            query += query.isEmpty() ? " lastname='" + user.getFirstName() + "'" : ", lastname='" + user.getFirstName() + "'";
        if(!user.getEmail().equals(selectedUser.getEmail()))
            query += query.isEmpty() ? " email='" + user.getEmail() + "'" : ", email='" + user.getEmail() + "'";
        if(!user.getPassword().isEmpty() && !user.getPassword().equals(selectedUser.getPassword()))
            query += query.isEmpty() ? " password='" + user.getPassword() + "'" : ", password='" + user.getPassword() + "'";
        if(!user.getPhone().equals(selectedUser.getPhone()))
            query += query.isEmpty() ? " phone='" + user.getPhone() + "'" : ", phone='" + user.getPhone() + "'";
        if(!user.getAddress().equals(selectedUser.getAddress()))
            query += query.isEmpty() ? " address='" + user.getAddress() + "'" : ", address='" + user.getAddress() + "'";
        if(query.isEmpty()) {
            showMessage("saveUserInfo", "...nothing update");
            return user;
        }
        if( sql.update("UPDATE users SET " + query + " WHERE id=" + user.getId() + " LIMIT 1") ) {
            showMessage("saveUserInfo", SAVE_OK);
            return user;
        }
        errorMessage("saveUserInfo", SAVE_FAILED);
        return null;
    }

    private void errorMessage(String moduleName, String msg) {
        this.errorMessage(this.getClass().getSimpleName(), moduleName, msg);
    }

    @Override
    public void errorMessage(String className, String moduleName, String msg) {
        lastMessage = "ERROR!!! " + this.getClass().getSimpleName() + " : " + moduleName + " -> " + msg;
        if(mainController != null) {
            switch (currentForm) {
                default:
                case MAIN:
                    mainController.setStatus(lastMessage);
                    break;
                case STUDENT:
                    studentController.setStatus(lastMessage);
                    break;
                case ADMIN:
                    adminController.setStatus(lastMessage);
                    break;
            }
        }
        System.out.println( lastMessage );
    }

    private void showMessage(String moduleName, String msg) {
        this.showMessage(this.getClass().getSimpleName(), moduleName, msg);
    }

    public void showMessage(String simpleName, String moduleName, String msg) {
        lastMessage = this.getClass().getSimpleName() + " : " + moduleName + " -> " + msg;
        if(mainController != null) {
            switch (currentForm) {
                default:
                case MAIN:
                    mainController.setStatus(msg);
                    break;
                case STUDENT:
                    studentController.setStatus(msg);
                    break;
                case ADMIN:
                    adminController.setStatus(msg);
                    break;
            }
        }
        System.out.println( lastMessage );
    }

    public List<UserInfo> adminFindUser(String text) {
        String sql_query = "";
        if(text != null && !text.isEmpty())
        {
            int ID = 0;
            try {
                ID = Integer.parseInt(text);
            } catch (NumberFormatException ex){
            }
            if (ID > 0)
                sql_query = "SELECT * FROM users WHERE id=" + ID + " LIMIT 1";
            else
                sql_query = "SELECT * FROM users WHERE email='" + text + "'" + " LIMIT 1";
            ResultSet rs = sql.query(sql_query);
            List<UserInfo> personList = new ArrayList<UserInfo>();
            try {
                while (rs != null && rs.next()) {
                    int id = rs.getInt("id");
                    String firstName = rs.getString("firstname");
                    String lastName = rs.getString("lastname");
                    String email = rs.getString("email");
                    int group_id = rs.getInt("group_id");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String pass = rs.getString("password");
                    UserInfo person = new UserInfo(id, firstName, lastName, email, group_id, phone, pass, address);
                    personList.add(person);
                }
            } catch (Exception ex) {
                errorMessage("adminFindUser", ex.getMessage());
                ex.printStackTrace();
            }
            return personList;
        }
        else
        {
            ResultSet rs = sql.query("select * from users where id > 0");
            List<UserInfo> personList = new ArrayList<UserInfo>();
            try {
                while (rs != null && rs.next()) {
                    int id = rs.getInt("id");
                    String firstName = rs.getString("firstname");
                    String lastName = rs.getString("lastname");
                    String email = rs.getString("email");
                    int group_id = rs.getInt("group_id");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String pass = rs.getString("password");
                    UserInfo person = new UserInfo(id, firstName, lastName, email, group_id, phone, pass, address);
                    personList.add(person);
                }
            } catch (Exception ex) {
                errorMessage("adminFindUser", ex.getMessage());
                ex.printStackTrace();
            }
            return personList;
        }
    }
}
