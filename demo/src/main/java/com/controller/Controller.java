package com.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.rmi.Naming;
import java.util.*;

import com.service.VideoServiceClient;

import com.model.StationInfo;

public class Controller {
    @FXML
    private GridPane cardsGrid;
    @FXML
    private VBox control;

    @FXML
    public void initialize() {
        TextField hostField = new TextField();
        TextField portField = new TextField();

        Button createButton = new Button();

        VBox hostCard = createHostCard( hostField, portField, createButton);

        cardsGrid.add(hostCard,0,0);

        createButton.setOnAction(event -> {
            cardsGrid.getChildren().clear();
            createCard(hostField, portField, createButton);
        });
    }

    private void createCard(TextField hostField, TextField portField, Button createButton){
        String host = hostField.getText();
        String port = portField.getText();
        String hostPort = "rmi://" + host + ":" + port + "/";
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                int numberObjetos = 0;

                while (true) {
                    try {
                        Thread.sleep(100);
                        String[] objetos = Naming.list(hostPort);
                        if (numberObjetos != objetos.length) {
                            int finalNumberObjetos = objetos.length;

                            Platform.runLater(() -> {
                                cardsGrid.getChildren().clear();
                                if (finalNumberObjetos == 0) loadErro(hostField, portField, createButton, hostPort);
                                else loadCard(objetos, host, port);
                            });
                            numberObjetos = finalNumberObjetos;
                        }
                    } catch (Exception e) {
                        if(numberObjetos == 0){
                            Platform.runLater(() -> loadErro(hostField, portField, createButton, hostPort));
                            numberObjetos = -1;
                        }
                        System.out.println("Erro: " + e.getMessage());
                    }
                }
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true); 
        thread.start();
    }

    private void loadCard(String[] objetos, String host, String port){
        List<StationInfo> stations = loadStations(objetos, host, port);

        int col = 2;
        for (int i = 0; i < stations.size(); i++) {
            loadCard(stations.get(i),col, i);
        }
    }

    private List<StationInfo> loadStations(String[] objetos, String host, String port){
        List<StationInfo> stations = new ArrayList<>();
        for (String obj : objetos) {
            String id = obj.substring(obj.lastIndexOf("/") + 1);
            stations.add(new StationInfo( id, host, port, "rmi:" + obj));
        }

        return stations;
    }

    private void loadCard(StationInfo est,int col, int i){
            VBox card = createCard(est);
            card.setUserData(est); 

            card.setOnMouseClicked(event -> {
                loadOptions(est.getHost(),est.getId(),est.getPort());
            });

            int colStations = i % col;
            int rowStations = i / col;
            cardsGrid.add(card, colStations, rowStations);
    }


    private void loadErro(TextField hostField, TextField portField, Button createButton, String hostPort){
        Label erroLabel = new Label("Erro de Conexão com o Servidor");
        erroLabel.getStyleClass().add("station-title");

        Label erroDescriptionLabel = new Label("Verifique se o servidor RMI está em execução e acessível no endereço "+ hostPort);
        erroDescriptionLabel.getStyleClass().add("station-description");

        Label buttonLabel = new Label("Voltar");
        buttonLabel.getStyleClass().add("button-text");

        HBox buttonBox = new HBox(10, buttonLabel);
        buttonBox.setAlignment(Pos.CENTER);

        Button backButton = new Button();
        backButton.setGraphic(buttonBox);

        backButton.getStyleClass().add("hero-button");

        backButton.setOnMouseClicked(event->{
            VBox hostCard = createHostCard( hostField, portField, createButton);
            cardsGrid.getChildren().clear();
            cardsGrid.add(hostCard,0,0);
        });

        VBox cardErro = new VBox(15,erroLabel, erroDescriptionLabel, backButton);
        cardErro.setAlignment(Pos.CENTER);
        cardErro.getStyleClass().add("station-card");
        cardsGrid.add(cardErro,0,0);
    }

    private void loadOptions(String host, String id, String port){
        control.getChildren().clear();
        control.getChildren().add(createControlPanel(host, id, port));
    }

    private VBox createControlPanel(String host, String id, String port) {
        Label controlHeader = new Label(id + " estação selecionada");
        controlHeader.getStyleClass().add("control-header");
        
        Button playButton = new Button("▶ Play");
        Button pauseButton = new Button("⏸ Pause");
        Button restartButton = new Button("🔄 Restart");
        
        playButton.getStyleClass().addAll("control-button", "play-button");
        pauseButton.getStyleClass().addAll("control-button", "pause-button");
        restartButton.getStyleClass().addAll("control-button", "restart-button");
        
        playButton.setPrefWidth(120);
        playButton.setPrefHeight(40);
        pauseButton.setPrefWidth(120);
        pauseButton.setPrefHeight(40);
        restartButton.setPrefWidth(120);
        restartButton.setPrefHeight(40);
        
        HBox buttonGroup = new HBox(10);
        buttonGroup.setAlignment(Pos.CENTER);
        buttonGroup.getChildren().addAll(playButton, pauseButton, restartButton);
        
        VideoServiceClient cliente = new VideoServiceClient(host, id, port);
        playButton.setOnAction(event -> cliente.initService(1));
        pauseButton.setOnAction(event -> cliente.initService(2));
        restartButton.setOnAction(event -> cliente.initService(3));
        
        VBox actionsContainer = new VBox(15);
        actionsContainer.setAlignment(Pos.CENTER);
        actionsContainer.getChildren().add(buttonGroup);
        
        VBox controlPanel = new VBox(controlHeader, actionsContainer);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.getStyleClass().add("control-panel");
        
        VBox wrapper = new VBox(controlPanel);
        wrapper.setSpacing(0);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPrefWidth(400);
        HBox.setHgrow(wrapper, Priority.NEVER);
        
        return wrapper;
    }

    private VBox createHostCard(TextField hostField, TextField portField, Button createButton) {
        VBox serviceBox = createHostField(hostField);
        VBox portBox = createPortField(portField);
        HBox fieldsLine = new HBox(24, serviceBox, portBox);
        fieldsLine.setAlignment(Pos.CENTER);

        VBox fieldsContainer = new VBox(32, fieldsLine);
        fieldsContainer.getStyleClass().add("fields-container");

        HBox actionZone = createActionZone(createButton);

        VBox card = new VBox(40, fieldsContainer, actionZone);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(650);
        card.getStyleClass().add("modern-card");

        return card;
    }

    private VBox createHostField(TextField hostField) {
        Label hostLabel = new Label("Endereço Host");
        hostLabel.getStyleClass().add("field-title");

        hostField.setPromptText("localhost");
        hostField.setText("localhost");
        hostField.getStyleClass().add("modern-input");

        Label hostTip = new Label("onde seu servidor vai rodar");
        hostTip.getStyleClass().add("field-tip");

        VBox hostBox = new VBox(12, new HBox(8, hostLabel), hostField, hostTip);
        hostBox.setFillWidth(true);
        HBox.setHgrow(hostBox, Priority.ALWAYS);
        return hostBox;
    }

    private VBox createPortField(TextField portField) {
        Label portLabel = new Label("Porta");
        portLabel.getStyleClass().add("field-title");

        portField.setPromptText("1099");
        portField.setText("1099");
        portField.getStyleClass().add("modern-input");

        Label portTip = new Label("porta padrão é 1099");
        portTip.getStyleClass().add("field-tip");

        VBox portBox = new VBox(12, new HBox(8, portLabel), portField, portTip);
        portBox.setFillWidth(true);
        HBox.setHgrow(portBox, Priority.ALWAYS);

        return portBox;
    }

    private HBox createActionZone(Button createButton) {
        Label createLabel = new Label("Procurar Host");
        createLabel.getStyleClass().add("button-text");

        HBox createBox = new HBox(10, createLabel);
        createBox.setAlignment(Pos.CENTER);

        createButton.setGraphic(createBox);
        createButton.getStyleClass().add("hero-button");

        HBox actionZone = new HBox(20, createButton);
        actionZone.setAlignment(Pos.CENTER);
        actionZone.getStyleClass().add("action-zone");

        return actionZone;
    }

    private VBox createCard(StationInfo est) {
        Label idLabel = new Label(est.getId());
        idLabel.getStyleClass().add("station-title");

        Label nameServe = new Label(est.getNameServe());
        nameServe.getStyleClass().add("station-description");

        Pane dot = new Pane();
        dot.setPrefSize(10, 10);

        Label host = new Label(est.getHost());
        host.getStyleClass().add("connection-text");

        HBox hostBox = new HBox(6, dot, host);
        hostBox.setAlignment(Pos.CENTER);

        HBox hostHBox = new HBox(10, hostBox);
        hostHBox.setAlignment(Pos.CENTER);

        VBox card = new VBox(15,idLabel, nameServe, hostHBox);
        card.setAlignment(Pos.CENTER);
 
        card.getStyleClass().add("station-card");

        return card;
    }
}