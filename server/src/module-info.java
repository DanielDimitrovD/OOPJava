module server {
    requires java.rmi;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens server.app to javafx.fxml;
    exports server.app to javafx.graphics;
    opens serverRMIDefinitions to java.rmi;
    exports userPackage;
    exports serverRMIDefinitions;
}
