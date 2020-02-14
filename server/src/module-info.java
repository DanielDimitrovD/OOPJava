module server {
    requires java.rmi; // for RMI object
    requires javafx.controls; // for FX
    requires javafx.fxml; // for FXML
    requires java.desktop; // for XMLSerialization

    opens server.app to javafx.fxml; // server.app uses fxml
    exports server.app to javafx.graphics; // give access to FX graphics
    opens serverRMIDefinitions to java.rmi; // uses RMI
    exports userPackage;
}
