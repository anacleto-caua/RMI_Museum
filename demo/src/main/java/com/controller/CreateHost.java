package com.controller;

import com.service.*;
import com.model.Host;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.io.File;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CreateHost {
    @FXML private TextField nameField;
    @FXML private TextField hostField;
    @FXML private TextField serviceField;
    @FXML private TextField portField;
    @FXML private VBox vboxContainer;
    @FXML private MediaView mediaView;   
    @FXML private ComboBox<String> videoComboBox;
    private VideoServiceHost serviceVideo; 
    private int indiceVideo = 0;

    @FXML
    public void initialize() {
        loadComboBox();
    }

    private File[] videoFolder(){
        File folder = new File("src/main/resources/video");
        return folder.listFiles();
    }

    private void loadComboBox(){
        File[] files = videoFolder();

        for (File file : files) {
            videoComboBox.getItems().add(file.getName());
        }

        videoComboBox.getSelectionModel().selectFirst();
        indiceVideo = videoComboBox.getSelectionModel().getSelectedIndex(); 

        videoComboBox.valueProperty().addListener((obs, oldVal, newVal) -> indiceVideo = videoComboBox.getSelectionModel().getSelectedIndex());
    }

    public void init(Stage stage) {
        stage.setOnCloseRequest(event->  {
            serviceVideo.closeHost();
            Platform.exit(); 
            try{
                Thread.sleep(1000);
                System.exit(0);
            }catch(Exception e){
                System.out.println("Erro ao encerrar");
            }
        });
    }

    @FXML
    public void createHost(ActionEvent event){
        try{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            init(stage);
            Host host = new Host(nameField.getText().trim(), hostField.getText().trim(), serviceField.getText().trim(), Integer.parseInt(portField.getText().trim()),indiceVideo);

            if(!checkFields(host)) loadErroFields();
            else if(checkID(host)) initHost(host);

            else loadErroID(host.getService());
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

    private boolean checkFields(Host host) {
        if (host.getHost() == null || host.getHost().isEmpty()) return false;
        if (host.getName() == null || host.getName().isEmpty()) return false;
        if (host.getService() == null || host.getService().isEmpty()) return false;
        if (host.getPort() <= 0) return false;
        return true;
    }

    private boolean checkID(Host host){
        String hostPort= "rmi://" + host.getHost() + ":" + host.getPort() + "/";
        try{
            String[] objetos = Naming.list(hostPort);
            for(String obj : objetos){
                String id = obj.substring(obj.lastIndexOf('/') + 1);
                if(host.getService().equals(id)) return false;
            }
        }catch (Exception e) {
            System.out.println("Erro: "+e);
        }
        return true;
    }

    private void loadErroID(String id){
        Alert alert = loadErro();
        alert.setTitle("Erro de Registro");
        alert.setHeaderText("ID já registrado");
        alert.setContentText("O ID \"" + id + "\" já está em uso no RMI.\nEscolha outro identificador.");

        alert.showAndWait();
    }

    private void loadErroFields(){
        Alert alert = loadErro();
        alert.setTitle("Campos inválidos");
        alert.setHeaderText("Preencha todos os campos corretamente");
        alert.setContentText("Verifique se todos os campos foram preenchidos e se a porta é válida.");

        alert.showAndWait();
    }

    private Alert loadErro(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().getStyleClass().add("modern-card");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/erro.css").toExternalForm());
        
        return alert;
    }

    private void initServiceVideo(Host host){
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        serviceVideo = new VideoServiceHost(host.getName(), host.getHost(), host.getService(), host.getPort(), mediaPlayer);
        serviceVideo.startHost();
    }

    private void initViewVideo() {
        vboxContainer.getChildren().clear();

        File[] files = videoFolder();

        File videoFile = files[indiceVideo];
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
