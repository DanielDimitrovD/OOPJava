package userPackage;

import java.io.Serializable;

// class representing a user in the system
public class User implements Serializable {
    private  String username;
    private  String password;
    private  Privileges privileges;

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
}
