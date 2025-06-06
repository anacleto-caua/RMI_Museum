package org.host.base;

import org.host.RemoteServiceImpl;
import org.services.RemoteService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * This class is used to instantiate and host a RemoteService
 */
public class ServiceHost {

    String name;

    int port;

    public ServiceHost(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public void startHost() {
        try {
            RemoteService service = new RemoteServiceImpl(this.name);

            LocateRegistry.createRegistry(this.port);
            System.out.println("RMI Registry starting on port: " + port);

            String host = "localhost"; // It's here till we find a better way of feeding a host name in
            String serviceName = "RemoteService"; // It's here till we find a better way of feeding a service name in

            Naming.rebind("rmi://" + host + ":" + port + "/" + serviceName, service);

            System.out.println("RemoteService is bound in RMI registry.");
            System.out.println("Server is ready and waiting for client calls...");
        } catch (Exception e) {
            System.err.println("ServiceHost exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
