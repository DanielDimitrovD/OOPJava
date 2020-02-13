package clientExperiment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Controller {
    private String hostName = "127.0.0.1";
    private int portNumber = 44444;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtResult;

    @FXML
    private Button btnLogin;

    @FXML
    void btnLoginClicked(ActionEvent event) throws IOException {
        try{
           String user = String.format("%s %s%n",txtUsername.getText(),txtPassword.getText());
        //    LoginUser user = new LoginUser(txtUsername.getText(),txtPassword.getText());
            Socket clientSocket = new Socket(hostName,portNumber);

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(user);
            String result = (String)in.readObject();

            txtResult.setText(result);

            if(in !=null)
                in.close();
            if(out != null)
                out.close();
            if(clientSocket != null)
                clientSocket.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() throws IOException {

        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'sample.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'sample.fxml'.";

    }
}
