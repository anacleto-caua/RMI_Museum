package org.test;// RMIDemoRunner.java

import org.client.ServiceClient;
import org.host.base.ServiceHost;

/**
 * This class is solely for the purpouse of TESTING,
 * it runs a client and host on the SAME MACHINE.
 */
public class RMIDemoRunner {
    public static void main(String[] args) {
        // Create and start the ServiceHost in a new thread
        Thread serverThread = new Thread(() -> {
            System.out.println("Starting RMI ServiceHost in a separate thread...");
            ServiceHost host = new ServiceHost("host1" , 1099);
            host.startHost();
        });
        serverThread.start();

        // Give the server a moment to initialize and bind the service
        try {
            System.out.println("Waiting 2 seconds for ServiceHost to initialize...");
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during sleep: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        // Now, execute the ServiceClient
        System.out.println("Executing RMI ServiceClient...");
        ServiceClient client = new ServiceClient();
        client.startClient(); // Call the main method of ServiceClient

        System.out.println("RMI Demo Runner finished!");
    }
}
