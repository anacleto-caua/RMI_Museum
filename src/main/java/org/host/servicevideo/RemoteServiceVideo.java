package org.host.servicevideo;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServiceVideo extends Remote {
    void control(int ctrl) throws RemoteException;
}