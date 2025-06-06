package org.test;// RMIDemoRunner.java

import org.client.ClientVideoService;
import org.host.servicevideo.HostVideoService;
import org.video_player.VideoPlayerApp;

/**
 * This class is solely for the purpouse of TESTING,
 * it runs a client and host on the SAME MACHINE.
 */
public class RMIDemoRunner {
    public static void main(String[] args) {

        VideoPlayerApp videoPlayerApp = new VideoPlayerApp();

        VideoPlayerApp.main(args);

        // Create and start the ServiceHost in a new thread
        Thread serverThread = new Thread(() -> {
            System.out.println("Starting RMI ServiceHost in a separate thread...");
            HostVideoService host = new HostVideoService("host1" , "localhost",  1099, "VideoService");
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
        ClientVideoService client = new ClientVideoService();
        client.startClient("localhost",  1099, "VideoService"); // Call the main method of ServiceClient

        System.out.println("RMI Demo Runner finished!");
    }
}
