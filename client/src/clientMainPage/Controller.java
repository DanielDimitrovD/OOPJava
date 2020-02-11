package clientMainPage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.sun.webkit.network.Util;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.ServerObjectInterface;

public class Controller {

    Registry registry  = LocateRegistry.getRegistry(12345);
    ServerObjectInterface server = (ServerObjectInterface)registry.lookup("ServerObjectInterfaceImplementation");
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnDecrypt;

    @FXML
    private Label lblEncrypt;

    @FXML
    private Label lblResults;

    @FXML
    private Label lblDercypt;

    @FXML
    private Button btnQuit;

    @FXML
    private TextField txtEncrypt;

    @FXML
    private TextField txtDecrypt;

    @FXML
    private TextField txtResults;

    public Controller() throws RemoteException, NotBoundException {
    }

    // method to create an alert with type,title,headerText,contextText for reuse
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
        assert btnEncrypt != null : "fx:id=\"btnEncrypt\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnDecrypt != null : "fx:id=\"btnDecrypt\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblEncrypt != null : "fx:id=\"lblEncrypt\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblDercypt != null : "fx:id=\"lblDercypt\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnQuit != null : "fx:id=\"btnQuit\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtEncrypt != null : "fx:id=\"txtEncrypt\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtDecrypt != null : "fx:id=\"txtDecrypt\" was not injected: check your FXML file 'sample.fxml'.";

    }
}
