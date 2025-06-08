module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires lombok;
    requires java.rmi;

    opens com to javafx.fxml;
    opens com.controller to javafx.fxml;

    exports com;
    exports com.controller to javafx.fxml;
    exports com.service to java.rmi;
}
