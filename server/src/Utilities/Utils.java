package Utilities;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.scene.control.Alert;
import userPackage.User;
import userPackage.Users;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;

public class Utils {

    // check username and password in Server Adding Tab for pattern
    public static boolean checkUserAndPassword(String username) { return username.matches("^[a-zA-Z]+\\d*$"); }

    // method to create an alert with type,title,headerText,contextText for reuse
    public static void showMessage(Alert.AlertType type, String title, String headerText, String contextText)  {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    // initialize XStream options
    public static void initXStream(XStream stream){
        stream.alias("Users", Users.class); // create alias to Users class
        stream.alias("User", User.class); // create alias to User class
        stream.addImplicitCollection(Users.class,"users"); // set Users as root tag
    }

    // method to write xml to file
    public static void writeInXML(File f, Users users){
        XStream stream = new XStream(new DomDriver()); // create Xstream object
        initXStream(stream); // initialize XStream options

        String process = stream.toXML(users); // convert data to XML format

        try(Formatter writer = new Formatter(new FileWriter(f))){
            writer.flush(); // flush writer
            writer.format("%s",process); // write data to file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // method to read XML from file
    public static Users readFromXML(File f) throws IOException {
        XStream xStream = new XStream(new DomDriver()); // initialize xstream object
        initXStream(xStream); // initialize xStream options

        Path path = Paths.get(String.valueOf(f)); // get file path
        if( Files.size(path) == 0){ // if file is empty
            return  new Users();
        }
        else { // file is not empty, have to read from it first
            FileReader reader = new FileReader(f);
            Users result = (Users)xStream.fromXML(reader); // deserialize from XML to Users class

            if(reader !=null) // close reader
                reader.close();
            return result;
        }
    }
}
