package com.controller;

import com.service.*;
import java.rmi.registry.LocateRegistry;

public class CreateHost {
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
