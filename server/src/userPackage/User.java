package userPackage;

import java.io.Serializable;

// class representing a user in the system
public class User {
    private  String username;
    private  String password;
    private  Privileges privileges;

    public User(){}

    public User(String username, String password, Privileges privileges) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
    }

    public final String getUsername() {
        return username;
    }
    public final String getPassword() {
        return password;
    }
    public final Privileges getPrivileges() {
        return privileges;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivileges(Privileges privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
       return String.format("Username:[ %s ]%nPassword:[ %s ]%nPrivileges:[ %s ]%n",username,password,privileges);
    }
}
