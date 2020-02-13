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

    // verify card number format
    public  static boolean verifyCardNumber(String cardNumber) {   // verify encryption input
        return cardNumber.matches("^[3-6]\\d{15}$");
    }

    // verify card number validity by Luhn Algorithm
    public  static boolean verifyLuhn(String cardNumber) {

        int sum = 0;  // accumulator
        int length = cardNumber.length(); // length of cardNumber
        boolean isSecondNumber = false; // boolean to represent every second number
        for( int i = length -1; i>=0; i--){

            int digit = cardNumber.charAt(i) - '0';  // converting char to int

            if( isSecondNumber == true)
                digit *= 2;   // multiply every second digit

            sum +=  digit%10 + digit/10;  // handle numbers with two digits
            isSecondNumber = !isSecondNumber;  //  change boolean on every iteration
        }
        return (sum % 10 ==0); // check if the sum fulfills the Luhn condition
    }

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
