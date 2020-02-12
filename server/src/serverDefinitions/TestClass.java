package serverDefinitions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import server.app.User;
import server.app.Users;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TestClass {
    public static void main(String[] args) throws FileNotFoundException {

        HashMap<String, String> userCredetentials = new HashMap<>();

        try (FileReader reader = new FileReader("data.xml")) { // read from file
            XStream stream = new XStream(new StaxDriver());  // initialize Xstream
            stream.alias("Users", Users.class);  // create alias for Users class
            stream.alias("user", User.class); // create alias for User class
            stream.addImplicitCollection(Users.class, "users");

            Users data = (Users) stream.fromXML(reader);  // serialize XML to Users class

            userCredetentials = new HashMap<>();
            for (User i : data.getUsers()) {    // fill HashMap with user values
                userCredetentials.put(i.getUsername(), i.getPassword());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        userCredetentials.forEach( (x,y) -> System.out.printf("%s-%s%n",x,y));
    }
}