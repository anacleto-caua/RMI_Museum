module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.rmi;

    opens com to javafx.fxml;
    exports com;

    exports com.controller to javafx.fxml;
}
