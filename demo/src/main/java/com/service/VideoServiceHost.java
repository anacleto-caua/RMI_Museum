package com.service;

import java.rmi.Naming;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class VideoServiceHost {
    private String name, host, service;
    private int port;

    public void startHost() {
        try {
            VideoServiceInterface serviceVideo = new VideoServiceImpl(name);

            String rmi = ("rmi://" + host + ":" + port + "/" + service);
            Naming.rebind(rmi, serviceVideo);

            System.out.println("RemoteServiceVideo is bound in RMI: [ "+ rmi +" ]...");

        } catch (Exception e) {
            System.err.println("ServiceHost exception: " + e.toString());
            e.printStackTrace();
        }
    }
}