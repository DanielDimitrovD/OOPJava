package clientLoginPage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import server.ServerObjectInterface;

public class Controller {
    Registry registry  = LocateRegistry.getRegistry(12345); // get registry
    ServerObjectInterface server = (ServerObjectInterface)registry.lookup("ServerObjectInterfaceImplementation"); // get server object

    @FXML
    private TabPane tpaneMenu;

    @FXML
    private TextField txtPassword;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgTitle;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private ImageView imgTitle1;

    @FXML
    private Label lblEncrypt;

    @FXML
    private TextField txtEncrypt;

    @FXML
    private Label lblDercypt;

    @FXML
    private TextField txtDecrypt;

    @FXML
    private Label lblResults;

    @FXML
    private TextField txtResults;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnQuit;

    @FXML
    private Button btnDecrypt;

    @FXML
    private Tab tabLogin;

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
                    "Enter card number for decryption again.");
            txtDecrypt.setText(""); // clear decryption textField
            txtResults.setText(""); // clear results textField
            txtDecrypt.requestFocus(); // request focus on decryption text field
        }
        else {   // input is valid ( 16 digits)
            txtResults.setText(server.decryptCardNumber(decryptionInput)); // set results textField to decrypted card number
            showMessage(Alert.AlertType.INFORMATION,"Decryption of card number","Decryption of card number successful",
                    "Results are shown in the Results text field.");
            txtResults.requestFocus(); // request focus on results textField
        }
    }

    @FXML
    void btnEncryptClicked(ActionEvent event) throws RemoteException {
        String encryptionInput = Utilities.removeWhiteSpaces(txtEncrypt.getText()); // remove whitespaces from input
        txtDecrypt.setText(""); // clear decryption textField
        if( !Utilities.verifyCardNumber(encryptionInput) || !Utilities.verifyLuhn(encryptionInput)){  // invalid card number
            showMessage(Alert.AlertType.WARNING,"Encryption of car number","Card number for encryption is INVALID!",
                    "Enter card number for encryption again.");
            txtEncrypt.setText(""); // clear encryption textField
            txtResults.setText(""); // clear results textField
            txtEncrypt.requestFocus(); // request focus on encryption text field
        }
        else {  // card number is valid
            txtResults.setText(server.encryptCardNumber(encryptionInput));  // set result text field to encrypted card number
            showMessage(Alert.AlertType.INFORMATION,"Encryption of card number","Encryption of card number successful!",
                    "Results are shown in the Results text field.");
            txtResults.requestFocus(); // request focus on the result textField
        }
    }
    @FXML
    void btnQuitClicked(ActionEvent event) throws RemoteException {
        showMessage(Alert.AlertType.INFORMATION,"Client main page","Exiting the system","");
        Platform.exit();
    }

    @FXML
    void initialize() {
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblTitle != null : "fx:id=\"lblTitle\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'sample.fxml'.";
        assert imgTitle != null : "fx:id=\"imgTitle\" was not injected: check your FXML file 'sample.fxml'.";

        tabOperations.setDisable(true); // disable Operations tab
    }
}
