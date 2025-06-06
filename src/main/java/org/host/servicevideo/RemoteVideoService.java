package org.host.servicevideo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteVideoService extends UnicastRemoteObject implements RemoteServiceVideo {

    String name;

    public RemoteVideoService(String name) throws RemoteException {
        super();
        this.name = name;

        System.out.println("RemoteVideoService created: " + name);
    }

    @Override
    public void PlayVideo() throws RemoteException {
        System.out.println("Server method: PlayVideo was called");
    }

    @Override
    public void StopVideo() throws RemoteException {
        System.out.println("Server method: StopVideo was called");
    }

    @Override
    public void RestartVideo() throws RemoteException {
        System.out.println("Server method: RestartVideo was called");
    }
}
