package server.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Users implements Serializable {
    List<User> users = new ArrayList<>();

    public final List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public final void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    public final void addUser( User u){
        this.users.add(u);
    }
}
