package server.app;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import DatabaseConnector.DatabaseAPI;
import Utilities.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import serverRMIDefinitions.*;
import userPackage.Person;
import userPackage.Privileges;
import userPackage.UserCardNumber;

import static Utilities.Utils.showMessage;

public class Controller {

    private ServerObjectInterface serverObjectInterface; // interface object
    private Registry registry; // registry to bind

    private DatabaseAPI connectionToDatabase;
    private ObservableList data;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private ComboBox<Privileges> cmbPrivilege;

    @FXML
    private TextArea txaLog;

    @FXML
    private TableView tbvTableView;

    // add account to database in server side
    @FXML
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

                if(serverObjectInterface.addUser(txtUsername.getText(), txtPassword.getText(), cmbPrivilege.getValue()))
                {// add user to database
                showMessage(Alert.AlertType.INFORMATION, "Adding account to server", "Account added successfully",
                       String.format("Username: {%s}%nPassword: {%s}%nPrivileges: {%s}%n",username,password,privilege));
                }
                else{
                    showMessage(Alert.AlertType.ERROR,"Adding account to server","Error adding account",
                            String.format("Account with username %s already exists.",txtUsername.getText()));
                }
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
            sb.append(String.format("%s%n","End of file"));
        } catch (FileNotFoundException e) {
            showMessage(Alert.AlertType.ERROR, "Table view of file", "No file found.",
                    "Check file configurations.");
        }
        Platform.runLater( ()-> txaLog.setText(sb.toString()));
    }

    // display data for bank cards and their encryption's sorted by encryption's
    @FXML
    void btnOpenByEncryptionClicked(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append("Reading data from file sorted by Encryption Card Number\n");

        File f = new File("sortedByEncryption.txt");

        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNext()) {
                sb.append(String.format("%s  %s%n", scanner.next(),scanner.next()));
            }
            sb.append(String.format("%s%n","End of file"));
        } catch (FileNotFoundException e) {
            showMessage(Alert.AlertType.ERROR, "Table view of file", "No file found.",
                    "Check file configurations.");
        }
        Platform.runLater( ()-> txaLog.setText(sb.toString()));
    }
    @FXML
    void initialize() throws IOException, NotBoundException, RemoteException, SQLException {
        ObservableList<Privileges> options = FXCollections.observableArrayList(
                Privileges.GUEST,Privileges.USER,Privileges.ADMIN
        );

        registry = LocateRegistry.getRegistry(12345);
        serverObjectInterface = (ServerObjectInterface) registry.lookup("ServerObjectInterfaceImplementation");
        connectionToDatabase = new DatabaseAPI();

        cmbPrivilege.setItems(options);
    }

    private void setTableViewUsersData() throws SQLException {
        tbvTableView.getItems().clear();
        tbvTableView.refresh();

        data = FXCollections.observableArrayList();
        ArrayList<Person> personList = connectionToDatabase.getPeopleDataFromDatabase();
        data.addAll(personList);
        tbvTableView.setItems(data);

    }

    private void setTableViewUsersColumns() throws SQLException{
        tbvTableView.getColumns().clear();
        tbvTableView.refresh();

        TableColumn usernameColumn = new TableColumn("Username");
        usernameColumn.setMinWidth(100);
        usernameColumn.setCellValueFactory( new PropertyValueFactory<Person,String>("username"));

        TableColumn passwordColumn = new TableColumn("Password");
        passwordColumn.setMinWidth(100);
        passwordColumn.setCellValueFactory( new PropertyValueFactory<Person,String>("password"));

        TableColumn privilegesColumn = new TableColumn("Privileges");
        privilegesColumn.setMinWidth(100);
        privilegesColumn.setCellValueFactory( new PropertyValueFactory<Person,String>("privileges"));

        tbvTableView.getColumns().addAll(usernameColumn,passwordColumn,privilegesColumn);
    }

    @FXML
    void btnViewUsersClicked(ActionEvent event) throws SQLException {
        setTableViewUsersColumns();
        setTableViewUsersData();
    }

    private void setTableViewCardNumberColumns() throws SQLException{
        tbvTableView.getColumns().clear();
        tbvTableView.refresh();

        TableColumn usernameColumn = new TableColumn("Username");
        usernameColumn.setMinWidth(100);
        usernameColumn.setCellValueFactory( new PropertyValueFactory<UserCardNumber,String>("username"));

        TableColumn cardNumberColumn = new TableColumn("Card Number");
        cardNumberColumn.setMinWidth(100);
        cardNumberColumn.setCellValueFactory( new PropertyValueFactory<UserCardNumber,String>("cardNumber"));

        TableColumn encryptionNumber = new TableColumn("Encrypted Number");
        encryptionNumber.setMinWidth(100);
        encryptionNumber.setCellValueFactory( new PropertyValueFactory<Person,String>("encryptionNumber"));

        tbvTableView.getColumns().addAll(usernameColumn,cardNumberColumn,encryptionNumber);
    }

    private void setTableViewCardNumberData() throws SQLException {
        tbvTableView.getItems().clear();
        tbvTableView.refresh();

        data = FXCollections.observableArrayList();
        ArrayList<UserCardNumber> userCardNumberArrayList = connectionToDatabase.getUserCardNumberDataFromDatabase();
        data.addAll(userCardNumberArrayList);
        tbvTableView.setItems(data);
    }

    @FXML
    void btnViewCardNumbersClicked(ActionEvent event) throws SQLException {
        setTableViewCardNumberColumns();
        setTableViewCardNumberData();
    }

}
