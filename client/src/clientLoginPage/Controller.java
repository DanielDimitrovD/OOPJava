package clientLoginPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    void btnLoginClicked(ActionEvent event) {

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
