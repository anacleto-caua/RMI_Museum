package org.client;
import org.services.RemoteService;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ServiceClient {
    public void startClient() {
        try {
            String host = "localhost";
            int port = 1099;
            String serviceName = "RemoteService";

            RemoteService service = (RemoteService) Naming.lookup("rmi://" + host + ":" + port + "/" + serviceName);
            System.out.println("Client successfully looked up RemoteService.");

            // Auto tests a bunch of services....
            String greeting = service.getGreeting("User");
            System.out.println("Result of getGreeting: " + greeting);

            int sum = service.calculateSum(50, 75);
            System.out.println("Result of calculateSum(50, 75): " + sum);

            String inverted = service.invertString("Java RMI Examples");
            System.out.println("Result of invertString('Java RMI Examples'): " + inverted);

        } catch (RemoteException e) {
            System.err.println("ServiceClient RemoteException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ServiceClient exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
