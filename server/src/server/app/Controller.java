package server.app;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.Scanner;

import Utilities.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import serverRMIDefinitions.*;
import userPackage.Privileges;

import static Utilities.Utils.showMessage;

public class Controller {

    private ServerObjectInterface serverObjectInterface; // interface object
    private Registry registry; // registry to bind

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
    @FXML
    private Label lblViewData;

    @FXML
    private TextArea txaLog;

    @FXML
    private Button btnOpenByEncryption;

    @FXML
    private Button btnOpenByCard;


    @FXML
    // add account to database in server side
    void btnAddAccountClicked(ActionEvent event) throws IOException {
        String username = txtUsername.getText();  // get username from form
        String password = txtPassword.getText(); // get password from form
        Privileges privilege = cmbPrivilege.getValue(); // get privilege from form

        if ( !Utils.checkUserAndPassword(username) || !Utils.checkUserAndPassword(password) || privilege == null) { // input is invalid
            txtUsername.setText(""); // clear username textField
            txtPassword.setText(""); // clear password textField
            cmbPrivilege.getSelectionModel().selectFirst(); // clear combobox options
            showMessage(Alert.AlertType.WARNING, "Adding account to server", "Invalid information entered",
                    "Please enter the form again.");
            txtUsername.requestFocus(); // request focus on username textField
        } else {  // input is valid

                serverObjectInterface.addUser(txtUsername.getText(), txtPassword.getText(), cmbPrivilege.getValue()); // add user to database
                showMessage(Alert.AlertType.INFORMATION, "Adding account to server", "Account added successfully",
                        String.format("Username: {%s}%nPassword: {%s}%nPrivileges: {%s}%n",username,password,privilege));
            }
    }

    @FXML
    void btnOpenByCardClicked( ActionEvent event) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append("Reading data from file sorted by Bank Card Numbers\n");

        File f = new File("sortedByCardNumber.txt");

        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNext()) {
                sb.append(String.format("%s  %s%n", scanner.next(),scanner.next()));
            }
        } catch (FileNotFoundException e) {
            showMessage(Alert.AlertType.ERROR, "Table view of file", "No file found.",
                    "Check file configurations.");
        }
        Platform.runLater( ()-> txaLog.setText(sb.toString()));
    }

    @FXML
    void btnOpenByEncryptionClicked(ActionEvent event) {

    }

    @FXML
    void initialize() throws IOException, NotBoundException, RemoteException {
        ObservableList<Privileges> options = FXCollections.observableArrayList(
                Privileges.GUEST,Privileges.USER,Privileges.ADMIN
        );

        registry = LocateRegistry.getRegistry(12345);
        serverObjectInterface = (ServerObjectInterface) registry.lookup("ServerObjectInterfaceImplementation");

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
