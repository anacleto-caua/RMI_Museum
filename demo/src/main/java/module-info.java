module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.rmi;
    requires static lombok;

    opens com to javafx.fxml;
    opens com.controller to javafx.fxml;
    opens com.view to javafx.fxml;

    exports com;
    exports com.view to javafx.graphics;
    exports com.controller to javafx.fxml;
    exports com.service to java.rmi;
}
