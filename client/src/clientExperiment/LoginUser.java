package clientExperiment;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private final String username;
    private final String password;
    public LoginUser(String u,String p){
        username = u;
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
