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

        initViewVideo();

        initServiceVideo(host);
    }

    private void initServiceVideo(Host host){
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        VideoServiceHost serviceVideo = new VideoServiceHost(host.getName(), host.getHost(), host.getService(), host.getPort(), mediaPlayer);
        serviceVideo.startHost();
    }

    private void initViewVideo(){
        vboxContainer.getChildren().clear();
        File videoFile = new File("src/main/resources/video.mp4");

        String videoUri = videoFile.toURI().toString();
        Media media = new Media(videoUri);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(700);
        mediaView.setFitHeight(400);
        mediaView.setPreserveRatio(true);

        vboxContainer.getChildren().add(mediaView);
    }
}
