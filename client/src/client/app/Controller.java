package client.app;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import clientUtils.Utilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import serverRMIDefinitions.ServerObjectInterface;

public class Controller {
    Registry registry;
    ServerObjectInterface server;

    @FXML
    private TabPane tpaneMenu;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblOperationsUsername;
    @FXML
    private TextField txtUsername;

    @FXML
    private ImageView imgTitle1;

    @FXML
    private TextField txtEncrypt;

    @FXML
    private TextField txtDecrypt;

    @FXML
    private Tab tabOperations;

    public Controller() throws RemoteException, NotBoundException {
    }

    @FXML
    void btnLoginClicked(ActionEvent event) throws IOException {
       String username = txtUsername.getText();
       String password = txtPassword.getText();
       if( username.length() == 0 || password.length() == 0) {
          showMessage(Alert.AlertType.WARNING,"Login panel","Username or password not entered","Please fill in the " +
                  "necessary fields.");
           return;
       }
       if(server.validateUser(username, password)){
           showMessage(Alert.AlertType.INFORMATION,"Login panel","Login in system successful!","Main functionality " +
                   "on Operations tab enabled.");

           tabOperations.setDisable(false); // enable tab Operations
           lblOperationsUsername.setText(txtUsername.getText()); // set Username on Operations tab
           tpaneMenu.getTabs().remove(0); // remove login tab
       }
       else{
           showMessage(Alert.AlertType.ERROR,"Login panel","Login unsuccessful!","Exiting program.");
           Platform.exit();
       }
    }

    private void showMessage(Alert.AlertType type,String title,String headerText,String contextText) throws RemoteException {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    @FXML
    void btnDecryptClicked(ActionEvent event) throws RemoteException {
        String decryptionInput = Utilities.removeWhiteSpaces(txtDecrypt.getText()); // remove whitespaces from input
        txtEncrypt.setText(""); // clear encryption textField
        if(!Utilities.verifyDecryptNumber(decryptionInput)) {// input is invalid ( !16 digits)
            showMessage(Alert.AlertType.WARNING,"Decryption of card number","Card number for decryption is INVALID!",
                    "Enter card number [16 digits] for decryption again.");
            txtDecrypt.setText(""); // clear decryption textField
            txtDecrypt.requestFocus(); // request focus on decryption text field
        }
        else {   // input is valid ( 16 digits)
          try {
            String result = server.decryptCardNumber(txtUsername.getText(),decryptionInput); // request for RMI method
            if( result != null) { // user has decryption functionality
                showMessage(Alert.AlertType.INFORMATION, "Decryption of card number", "Information about decryption operation",
                        result);
            }
        } catch (RemoteException | SQLException e) {
              showMessage(Alert.AlertType.ERROR,"Encryption of card number","Server host unavailable.",
                      "Please try again later.");
              Platform.exit();
              System.exit(0);
          }
        }
    }

    @FXML
    void btnEncryptClicked(ActionEvent event) throws RemoteException {
        String encryptionInput = Utilities.removeWhiteSpaces(txtEncrypt.getText()); // remove whitespaces from input
        txtDecrypt.setText(""); // clear decryption textField
        if( !Utilities.verifyCardNumber(encryptionInput)){  // invalid card number
            showMessage(Alert.AlertType.WARNING,"Encryption of car number","Card number for encryption is INVALID!",
                    "Enter card number for encryption again.");
            txtEncrypt.setText(""); // clear encryption textField
            txtEncrypt.requestFocus(); // request focus on encryption text field
        }
        else {  // card number is valid
            try {
                String result = server.encryptCardNumber(txtUsername.getText(), encryptionInput);
                if (result != null) {  // if user has privileges to use encryption method of RMI
                    showMessage(Alert.AlertType.INFORMATION,"Encryption of card number","Information about current operation.",
                            result);
                }
            } catch (RemoteException | SQLException e) {
                showMessage(Alert.AlertType.ERROR,"Encryption of card number","Server host unavailable.",
                        "Please try again later.");
                Platform.exit();
                System.exit(0);
            }
            }
        }
    @FXML
    void btnQuitClicked(ActionEvent event) throws RemoteException {
        showMessage(Alert.AlertType.INFORMATION,"Client main page","Exiting the system","");
        Platform.exit();
        System.exit(0);
    }


    @FXML
    void initialize() throws RemoteException {
        try{
            registry = LocateRegistry.getRegistry(12345);
            server = (ServerObjectInterface)registry.lookup("ServerObjectInterfaceImplementation");
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            showMessage(Alert.AlertType.ERROR,"Connection to login panel","Error connecting to server.","" +
                    "Please try again later");
            Platform.exit();
            System.exit(0);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        tabOperations.setDisable(true); // disable Operations tab
    }
}
