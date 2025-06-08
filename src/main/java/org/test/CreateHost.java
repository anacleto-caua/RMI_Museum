package org.test;

import org.client.ClientVideoService;
import org.host.servicevideo.HostVideoService;
import java.rmi.registry.LocateRegistry;

public class CreateHost {
    public static void main(String[] args) {
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

        HostVideoService serviceVideo = new HostVideoService(name, host, service, port);
        serviceVideo.startHost();
        
        try {
            System.out.println("Waiting 2 seconds for ServiceHost to initialize...");
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during sleep: " + e.getMessage());
            Thread.currentThread().interrupt(); 
        }
        int teste = 2;
        ClientVideoService cliente = new ClientVideoService(host, service, port);
        cliente.initService(teste);
    }
}

