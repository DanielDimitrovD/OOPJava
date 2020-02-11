package serverG;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.ResourceBundle;

import com.sun.glass.ui.Pixels;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.Privileges;

public class Controller {

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

            XStream xStream = new XStream(new DomDriver()); // initialize xstream object
            xStream.alias("Users",Users.class);  // create alias for Users class
            xStream.alias("user",User.class); // create alias for User class
            xStream.addImplicitCollection(Users.class,"users");

            User user = new User(username,password,privilege); // create a user instance

            Path path = Paths.get("data.xml");
            if(!Files.exists(path)) {              // creating file for first time
                Files.createFile(Paths.get("data.xml"));
                try( Formatter writer = new Formatter(new FileWriter("./data.xml"))){
                    writer.flush(); // flush writer
                    Users users = new Users(); // create Users class
                    users.addUser(user); // add user to Users class

                    String xml = xStream.toXML(users); // serialize object
                    writer.format("%s",xml); // write xml to the file
                }
            }

            else {  // data file exists

                FileReader reader = new FileReader("./data.xml"); // open data file to read from
                Users users = (Users)xStream.fromXML(reader); // deserialize Users class
                users.addUser(user); // add user to the Users class

                if(reader != null)
                    reader.close();

                try(BufferedWriter writer = new BufferedWriter(new FileWriter("./data.xml"))){
                    //writer.flush(); // flush writer
                    String xml = xStream.toXML(users); // serialize object
                    writer.write(xml); // write to file
                }
            }
            showMessage(Alert.AlertType.INFORMATION,"Adding account to server","Account added successfully","");
        }
    }

    @FXML
    void initialize() throws IOException {
        ObservableList<Privileges> options = FXCollections.observableArrayList(
                Privileges.NONE,Privileges.ENCRYPT,Privileges.DECRYPT,Privileges.BOTH
        );

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
