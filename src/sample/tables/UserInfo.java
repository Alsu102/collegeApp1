package sample.tables;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserInfo {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    public IntegerProperty idProperty() {
        return id;
    }
    public final int getId() {
        return idProperty().get();
    }
    public final void setId(int id) {
        idProperty().set(id);
    }

    private final StringProperty firstName = new SimpleStringProperty(this, "firstName");
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public final String getFirstName() {
        return firstNameProperty().get();
    }
    public final void setFirstName(String firstName) {
        firstNameProperty().set(firstName);
    }

    private final StringProperty lastName = new SimpleStringProperty(this, "lastName");
    public StringProperty lastNameProperty() {
        return lastName;
    }
    public final String getLastName() {
        return lastNameProperty().get();
    }
    public final void setLastName(String lastName) {
        lastNameProperty().set(lastName);
    }

    private final StringProperty email = new SimpleStringProperty(this, "email");
    public StringProperty emailProperty() {
        return email;
    }
    public final String getEmail() {
        return emailProperty().get();
    }
    public final void setEmail(String email) {
        emailProperty().set(email);
    }

    private final IntegerProperty group_id = new SimpleIntegerProperty(this, "group_id");
    public IntegerProperty groupIdProperty() {
        return group_id;
    }
    public final int getGroupId() {
        return groupIdProperty().get();
    }
    public final void setGroupId(int group_id) {
        groupIdProperty().set(group_id);
    }

    private final StringProperty phone = new SimpleStringProperty(this, "phone");
    public StringProperty phoneProperty() {
        return phone;
    }
    public final String getPhone() {
        return phoneProperty().get();
    }
    public final void setPhone(String phone) {
        phoneProperty().set(phone);
    }

    private final StringProperty password = new SimpleStringProperty(this, "password");
    public StringProperty passwordProperty() {
        return password;
    }
    public final String getPassword() {
        return passwordProperty().get();
    }
    public final void setPassword(String password) {
        passwordProperty().set(password);
    }

    private final StringProperty address = new SimpleStringProperty(this, "address");
    public StringProperty addressProperty() {
        return address;
    }
    public final String getAddress() {
        return addressProperty().get();
    }
    public final void setAddress(String address) {
        addressProperty().set(address);
    }

    public UserInfo() {}

    public UserInfo(int id,
            String firstName,
            String lastName,
            String email,
            int group_id,
            String phone,
            String password,
            String address) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setGroupId(group_id);
        setPhone(phone);
        setPassword(password);
        setAddress(address);
    }

    public static int getGroupId(String selectedRole) {
        int group_id = 0;
        switch (selectedRole.toLowerCase().trim())
        {
            default:
            case "guest":
                group_id = 2;
                break;
            case "student":
                group_id = 3;
                break;
            case "instructor":
                group_id = 4;
                break;
            case "admin":
                group_id = 1;
                break;
        }
        return group_id;
    }

    public static String getRoleName(int group_id) {
        switch (group_id)
        {
            default:
            case 2:
                return "guest";
            case 3:
                return "student";
            case 4:
                return "instructor";
            case 1:
                return "admin";
        }
    }

    public String whoaim() {
        return getRoleName(this.getGroupId());
    }
}
