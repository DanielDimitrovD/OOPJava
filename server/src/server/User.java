package server;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private final String username;
    private final String password;
    private ArrayList<String> accounts;
    private final Privileges privileges;

    public User(String username, String password, ArrayList<String> accounts, Privileges privileges) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>(accounts);
        this.privileges = privileges;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void setAccounts(ArrayList<String> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public Privileges getPrivileges() {
        return privileges;
    }
}
