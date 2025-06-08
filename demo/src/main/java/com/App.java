package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

import com.service.VideoServiceClient;

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
        //launch();
         try {
            System.out.println("Waiting 2 seconds for ServiceHost to initialize...");
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during sleep: " + e.getMessage());
            Thread.currentThread().interrupt(); 
        }

        int teste = 1;
        VideoServiceClient cliente = new VideoServiceClient("localhost", "ff", 1099);
        cliente.initService(teste);
    }
}