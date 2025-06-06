package org.services;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote {
    String getGreeting(String name) throws RemoteException;

    int calculateSum(int a, int b) throws RemoteException;

    String invertString(String inputString) throws RemoteException;
}