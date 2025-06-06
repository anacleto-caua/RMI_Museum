package org.host.servicevideo;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServiceVideo extends Remote {

    public void PlayVideo() throws RemoteException;

    public void StopVideo() throws RemoteException;

    public void RestartVideo() throws RemoteException;
}