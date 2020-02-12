package server.app;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.ResourceBundle;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import serverDefinitions.Privileges;
import serverDefinitions.User;
import serverDefinitions.Users;

public class Controller {

    private String pathToCredentials; // path to XML file with user data
    private boolean isEmpty;  // if XML file is empty

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lblAccountInfo;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblPrivilege;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private ComboBox<Privileges> cmbPrivilege;

    @FXML
    private Button btnAddAccount;

    @FXML
    private Label lblExport;

    @FXML
    private Label lblEncryption;

    @FXML
    private Label lblCardNumber;

    @FXML
    private Button btnSaveByEncryption;

    @FXML
    private Button btnSaveByCardNumber;

    @FXML
    private Button btnExit;


    // method to create an alert with type,title,headerText,contextText for reuse
    private void showMessage(Alert.AlertType type, String title, String headerText, String contextText)  {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    // initialize XStream options
    private void initXStream(XStream stream){
        stream.alias("Users",Users.class); // create alias to Users class
        stream.alias("User",User.class); // create alias to User class
        stream.addImplicitCollection(Users.class,"users"); // set Users as root tag
    }

    // method to write xml to file
    private void writeInXML(File f,Users users){
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
    private Users readFromXML(File f) throws IOException {
        XStream xStream = new XStream(new DomDriver()); // initialize xstream object
        initXStream(xStream); // initialize xStream options

        FileReader reader = new FileReader(f);
        Users result = (Users)xStream.fromXML(reader); // deserialize from XML to Users class

        if(reader !=null) // close reader
            reader.close();
        return result;
    }

    @FXML
    // add account to database in server side
    void btnAddAccountClicked(ActionEvent event) throws IOException {
        String username = txtUsername.getText();  // get username from form
        String password = txtPassword.getText(); // get password from form
        Privileges privilege = cmbPrivilege.getValue(); // get privilege from form

        if( username.length() == 0 || password.length() == 0 || privilege == null){ // input is invalid
            txtUsername.setText(""); // clear username textField
            txtPassword.setText(""); // clear password textField
            cmbPrivilege.getSelectionModel().selectFirst(); // clear combobox options
            showMessage(Alert.AlertType.WARNING,"Adding account to server","Invalid information entered",
                    "Please enter the form again.");
            txtUsername.requestFocus(); // request focus on username textField
        }
        else {  // input is valid

         /*   XStream xStream = new XStream(new DomDriver()); // initialize xstream object
            xStream.alias("Users", Users.class);  // create alias for Users class
            xStream.alias("user", User.class); // create alias for User class
            xStream.addImplicitCollection(Users.class,"users");
        */
           /*     try( Formatter writer = new Formatter(new FileWriter("serverData/data.xml"))){
                    writer.flush(); // flush writer
                    Users users = new Users(); // create Users class
                    users.addUser(user); // add user to Users class

                    String xml = xStream.toXML(users); // serialize object
                    writer.format("%s",xml); // write xml to the file
             */

            User user = new User(username,password,privilege); // create a user instance

            if(isEmpty) {  // file is empty, only write in file

                Users users = new Users(); // create Users instance
                users.addUser(user); // add User to empty Users list
                writeInXML(new File(pathToCredentials),users); // serialize Users class in XML in file
            }

            else {  // file is not empty, have to read from it first

                Users users = readFromXML(new File(pathToCredentials)); // read from XML and convert to Users class
                users.addUser(user); // add user to Users class
                writeInXML(new File(pathToCredentials),users); // write back to XML file

      /*          FileReader reader = new FileReader("serverData/data.xml"); // open data file to read from
                Users users = (Users)xStream.fromXML(reader); // deserialize Users class
                users.addUser(user); // add user to the Users class


                try(BufferedWriter writer = new BufferedWriter(new FileWriter("./data.xml"))){
                    //writer.flush(); // flush writer
                    String xml = xStream.toXML(users); // serialize object
                    writer.write(xml); // write to file
                }*/
            }
            showMessage(Alert.AlertType.INFORMATION,"Adding account to server","Account added successfully","");
        }
    }

    @FXML
    void initialize() throws IOException {
        ObservableList<Privileges> options = FXCollections.observableArrayList(
                Privileges.NONE,Privileges.ENCRYPT,Privileges.DECRYPT,Privileges.BOTH
        );
        pathToCredentials = "D:\\encryptionProject\\server\\src\\serverData\\data.xml";
        isEmpty = ( Files.size(Paths.get(pathToCredentials)) == 0); // check if file is empty
        assert lblAccountInfo != null : "fx:id=\"lblAccountInfo\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblUsername != null : "fx:id=\"lblUsername\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblPassword != null : "fx:id=\"lblPassword\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblPrivilege != null : "fx:id=\"lblPrivilege\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'sample.fxml'.";
        assert cmbPrivilege != null : "fx:id=\"cmbPrivilege\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnAddAccount != null : "fx:id=\"btnAddAccount\" was not injected: check your FXML file 'sample.fxml'.";

        cmbPrivilege.setItems(options);
    }
}
