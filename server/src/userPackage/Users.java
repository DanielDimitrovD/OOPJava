package userPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


// class wrapper of User
public class Users {
    List<User> users = new ArrayList<>();

    public Users(){}

    public final List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<User> users) {
        this.users = new ArrayList<User>(users);
    }

    public final void addUser(User u){
        this.users.add(u);
    }
}
