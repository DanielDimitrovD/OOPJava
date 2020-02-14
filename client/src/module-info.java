module client {
    requires java.rmi;
    requires javafx.controls;
    requires javafx.fxml;
    requires server;

    opens client.app to javafx.fxml;
    exports client.app to javafx.graphics;
}