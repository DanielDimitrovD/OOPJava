package clientMainPage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Label lblDercypt;

    @FXML
    private Button btnQuit;

    @FXML
    private TextField txtEncrypt;

    @FXML
    private TextField txtDecrypt;

    public Controller() throws RemoteException, NotBoundException {
    }

    @FXML
    void btnDecryptClicked(ActionEvent event) throws RemoteException {

    }

    @FXML
    void btnEncryptClicked(ActionEvent event) throws RemoteException {
        String encryptedText = txtEncrypt.getText().trim();
        if( encryptedText.length() != 0)
            txtDecrypt.setText(server.encryptCardNumber(encryptedText));
        else
            txtDecrypt.setText("Wrong number");
    }

    @FXML
    void btnQuitClicked(ActionEvent event) {
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
