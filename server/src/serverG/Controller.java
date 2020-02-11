package serverG;

import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Formatter;
import java.util.ResourceBundle;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import server.Privileges;
import server.User;

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
    void btnAddAccountClicked(ActionEvent event) {
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
            User user = new User(username,password,privilege); // create// a user instance

            XStream xStream = new XStream(new DomDriver()); // initialize xstream object
            xStream.alias("Username",User.class);

            String xml = xStream.toXML(user);

            FileChooser fc = new FileChooser(); // create a file chooser
            Formatter formatter;

            try {
                File f = fc.showOpenDialog(null);
                formatter = new Formatter(f);
                formatter.format("%s",xml);

                showMessage(Alert.AlertType.INFORMATION,"Adding account to server","Account added successfully",
                        "");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    @FXML
    void initialize() {
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
