package org.host;
import org.services.RemoteService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServiceImpl extends UnicastRemoteObject implements RemoteService {

    String name;

    RemoteServiceImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String getGreeting(String name) throws RemoteException {
        System.out.println("Server received getGreeting call for: " + name);
        return "Hello, " + name + "! Welcome to RMI.";
    }

    @Override
    public int calculateSum(int a, int b) throws RemoteException {
        System.out.println("Server received calculateSum call for: " + a + " and " + b);
        return a + b;
    }

    @Override
    public String invertString(String inputString) throws RemoteException {
        System.out.println("Server received invertString call for: " + inputString);
        return new StringBuilder(inputString).reverse().toString();
    }
}
