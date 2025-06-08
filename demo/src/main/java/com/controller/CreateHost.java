package com.controller;

import com.service.*;
import com.model.Host;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.*;

import java.io.File;
import java.rmi.registry.LocateRegistry;

public class CreateHost {
    @FXML private TextField nameField;
    @FXML private TextField hostField;
    @FXML private TextField serviceField;
    @FXML private TextField portField;
    @FXML private VBox vboxContainer;
    @FXML private MediaView mediaView;
    
    @FXML
    public void createHost(){
        try{
            Host host = new Host(nameField.getText().trim(), hostField.getText().trim(), serviceField.getText().trim(), Integer.parseInt(portField.getText().trim()));

            initHost(host);
        } catch (NumberFormatException e) {
            System.out.println("Erro"+ "A porta deve ser um número válido.");
        } catch (Exception e) {
            System.out.println("Erro"+ "Erro ao criar host: " + e.getMessage());
        }
    }

    private void initHost(Host host){
        try {
            LocateRegistry.createRegistry(host.getPort());
            System.out.println("RMI Registry criado na porta: " + host.getPort());
        } catch (Exception e) {
            System.out.println("Registry já estava rodando na porta " + host.getPort());
        }

        initServiceVideo(host);

        initViewVideo();
    }

    private void initServiceVideo(Host host){
        VideoServiceHost serviceVideo = new VideoServiceHost(host.getName(), host.getHost(), host.getService(), host.getPort());
        serviceVideo.startHost();
    }

    private void initViewVideo(){
        vboxContainer.getChildren().clear();
        File videoFile = new File("src/main/resources/video.mp4");

        String videoUri = videoFile.toURI().toString();
        Media media = new Media(videoUri);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(700);
        mediaView.setFitHeight(400);
        mediaView.setPreserveRatio(true);

        vboxContainer.getChildren().add(mediaView);
    }

 /*    public CreateHost() {
        String name = "name";
        String host = "localhost";
        String service = "service";
        int port = 1099;

        try {
            LocateRegistry.createRegistry(port);
            System.out.println("RMI Registry criado na porta: " + port);
        } catch (Exception e) {
            System.out.println("Registry já estava rodando na porta " + port);
        }

        VideoServiceHost serviceVideo = new VideoServiceHost(name, host, service, port);
        serviceVideo.startHost();
        
        try {
            System.out.println("Waiting 2 seconds for ServiceHost to initialize...");
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during sleep: " + e.getMessage());
            Thread.currentThread().interrupt(); 
        }
        int teste = 2;
        VideoServiceClient cliente = new VideoServiceClient(host, service, port);
        cliente.initService(teste);
    }
        */
}
