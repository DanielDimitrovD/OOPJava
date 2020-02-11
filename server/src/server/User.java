package server;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final Privileges privileges;

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
