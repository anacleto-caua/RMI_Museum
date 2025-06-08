package com.service;

import java.rmi.Naming;
import java.rmi.RemoteException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VideoServiceClient {
    private String host,serviceName;
    private int port;

    public void initService(int ctrl) {
        String rmi = "rmi://" + host + ":" + port + "/" + serviceName;
        try {
           VideoServiceInterface service = (VideoServiceInterface) Naming.lookup(rmi);
            service.control(ctrl);          
        } catch (RemoteException e) {
            System.err.println("ServiceClient RemoteException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ServiceClient exception: " + e.toString());
            e.printStackTrace();
        }
    }
}