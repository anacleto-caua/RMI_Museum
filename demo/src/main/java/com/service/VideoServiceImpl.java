package com.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;

public class VideoServiceImpl extends UnicastRemoteObject implements VideoServiceInterface {
    private String name;
    private MediaPlayer mediaPlayer;

    public VideoServiceImpl(String nameService, MediaPlayer mediaPlayer) throws RemoteException {
        this.name = nameService;
        this.mediaPlayer = mediaPlayer;

        System.out.println("RemoteVideoService created: " + name);
    }

    private void playVideo(){
        Platform.runLater(() -> {
            if(mediaPlayer.getCurrentTime().equals(mediaPlayer.getTotalDuration())) restartVideo();
            else mediaPlayer.play();
            System.out.println("Video started playing");
        });
    }

    private void stopVideo(){
        Platform.runLater(() -> {
            mediaPlayer.pause();
            System.out.println("Video paused");
        });
    }

    private void restartVideo(){
        Platform.runLater(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            System.out.println("Video restarted");
        });
    }

    @Override 
    public void control(int ctrl) {
        switch(ctrl) {
            case 1:
                playVideo();
                break;
            case 2:
                stopVideo();
                break;
            case 3:
                restartVideo();
                break;
            default:
                System.out.println("Comando inválido.");
        }
    }
}