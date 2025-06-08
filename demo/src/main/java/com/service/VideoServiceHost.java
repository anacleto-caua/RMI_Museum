package com.service;

import java.rmi.Naming;

import javafx.fxml.FXML;
import javafx.scene.media.MediaPlayer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class VideoServiceHost {
    private String name, host, service;
    private int port;
    
    @FXML private MediaPlayer mediaPlayer;

    public void startHost() {
        try {

            VideoServiceInterface serviceVideo = new VideoServiceImpl(name,mediaPlayer);

            String rmi = ("rmi://" + host + ":" + port + "/" + service);
            Naming.rebind(rmi, serviceVideo);

            System.out.println("RemoteServiceVideo is bound in RMI: [ "+ rmi +" ]...");

        } catch (Exception e) {
            System.err.println("ServiceHost exception: " + e.toString());
            e.printStackTrace();
        }
    }
}