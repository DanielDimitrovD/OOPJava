module client {
    requires java.rmi;
    requires javafx.controls;
    requires javafx.fxml;
    requires server;
    requires java.sql;

    opens client.app to javafx.fxml;
    exports client.app to javafx.graphics;
}