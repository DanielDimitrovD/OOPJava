package clientLoginPage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import clientMainPage.ClientMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import server.ServerObjectInterface;

public class Controller {
    Registry registry  = LocateRegistry.getRegistry(12345); // get registry
    ServerObjectInterface server = (ServerObjectInterface)registry.lookup("ServerObjectInterfaceImplementation"); // get server object

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtPassword;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgTitle;

    public Controller() throws RemoteException, NotBoundException {
    }

    @FXML
    void btnLoginClicked(ActionEvent event) throws IOException {
       String username = txtUsername.getText();
       String password = txtPassword.getText();
       if( username.length() == 0 || password.length() == 0)
           return;

       if(server.validateUser(username, password)){
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Login panel");
           alert.setHeaderText("Login in system successful!");
           alert.setContentText("Redirecting to main page.");
           alert.showAndWait();

           Parent root = FXMLLoader.load(getClass().getResource("../clientMainPage/sample.fxml"));
           Scene scene = new Scene(root);

           Stage stage = new Stage();
           stage.setTitle("Client Main Page");
           stage.setScene(scene);
           stage.show();

           ((Node)(event.getSource())).getScene().getWindow().hide();
       }
       else{
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText("Wrong credits");
           alert.showAndWait();
           Platform.exit();
       }
    }

    @FXML
    void initialize() {
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'sample.fxml'.";
        assert lblTitle != null : "fx:id=\"lblTitle\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'sample.fxml'.";
        assert imgTitle != null : "fx:id=\"imgTitle\" was not injected: check your FXML file 'sample.fxml'.";
    }
}
