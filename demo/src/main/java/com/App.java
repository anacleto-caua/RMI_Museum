package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        scene = new Scene(loadFXML("src/main/java/com/view/host.fxml"), width, height);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    private static Parent loadFXML(String fxml) throws IOException {
        File fxmlFile = new File(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile.toURI().toURL());
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}