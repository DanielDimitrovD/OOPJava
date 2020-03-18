package userPackage;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty privileges;

    public Person(String username, String password, String privileges) {
       this.username = new SimpleStringProperty(username);
       this.password = new SimpleStringProperty(password);
       this.privileges = new SimpleStringProperty(privileges);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPrivileges() {
        return privileges.get();
    }

    public SimpleStringProperty privilegesProperty() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges.set(privileges);
    }

    @Override
    public String toString() {
        return String.format("Username: %-10sPassword:%-10s Privileges:%-10s",getUsername(),getPassword(),getPrivileges());
    }
}
