package org.client;

import org.host.servicevideo.RemoteServiceVideo;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ClientVideoService {

    public void startClient(String host, int port, String serviceName) {
        String rmi = "rmi://" + host + ":" + port + "/" + serviceName;

        startClient(rmi);
    }

    public void startClient(String rmi) {
        try {
            RemoteServiceVideo service = (RemoteServiceVideo) Naming.lookup(rmi);
            System.out.println("Client successfully looked up Services.");

            // Auto tests a bunch of services....
            service.PlayVideo();
            service.StopVideo();
            service.RestartVideo();

        } catch (RemoteException e) {
            System.err.println("ServiceClient RemoteException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ServiceClient exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
