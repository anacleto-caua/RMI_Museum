package org.host.servicevideo;

import java.rmi.Naming;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class HostVideoService {
    private String name, host, service;
    private int port = 0000;

    public void startHost() {
        try {
            RemoteServiceVideo serviceVideo = new RemoteVideoService(name);

            String rmi = ("rmi://" + host + ":" + port + "/" + service);
            Naming.rebind(rmi, serviceVideo);

            System.out.println("RemoteServiceVideo is bound in RMI: [ "+ rmi +" ]...");

        } catch (Exception e) {
            System.err.println("ServiceHost exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
