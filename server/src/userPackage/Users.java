package userPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


// class wrapper of User
public class Users implements Serializable {
    List<User> users = new ArrayList<>();

    public final List<User> getUsers() {
        return new ArrayList<>(users);
    }
    public final void addUser( User u){
        this.users.add(u);
    }
}
