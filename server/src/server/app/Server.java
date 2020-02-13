package server.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import serverRMIDefinitions.ServerObjectInterface;
import serverRMIDefinitions.ServerObjectInterfaceImplementation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        ServerObjectInterface server = new ServerObjectInterfaceImplementation(); // create Interface instance
        Registry registry = LocateRegistry.createRegistry(12345); //  create registry
        registry.bind("ServerObjectInterfaceImplementation",server); // bind registry to object

        System.out.println("Object registered successfully.");

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Server control panel");
        stage.setScene(scene);
        stage.setOnCloseRequest( event-> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
