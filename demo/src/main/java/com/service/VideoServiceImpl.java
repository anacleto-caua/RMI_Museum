package com.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VideoServiceImpl extends UnicastRemoteObject implements VideoServiceInterface {
    private String name;

    public VideoServiceImpl(String nameService) throws RemoteException {
        this.name = nameService;

        System.out.println("RemoteVideoService created: " + name);
    }

    private void playVideo(){
        System.out.println("Play video");
    }

    private void stopVideo(){
        System.out.println("Stop Video");
    }

    private void restartVideo(){
        System.out.println("Restar video");
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