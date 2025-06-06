package org.host.servicevideo;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * This class is used to instantiate and host a RemoteService
 */
public class HostVideoService {

    private String name = "noname";

    private String host = "nohost";

    private int port = 0000;

    private String serviceName = "noservice";

    private String rmi = "normi";

    public HostVideoService(String name, String host, int port, String serviceName) {
        this.setName(name);
        this.setHost(host);
        this.setPort(port);
        this.setServiceName(serviceName);
    }

    public void startHost() {
        try {
            RemoteServiceVideo service = new RemoteVideoService(this.getName());

            LocateRegistry.createRegistry(this.getPort());
            System.out.println("RMI Registry starting on port: " + getPort());

            this.setRmi("rmi://" + getHost() + ":" + getPort() + "/" + getServiceName());
            Naming.rebind(this.getRmi(), service);

            System.out.println("RemoteServiceVideo is bound in RMI: [ "+ this.getRmi() +" ]...");
            System.out.println();
            System.out.println();
            System.out.println();

        } catch (Exception e) {
            System.err.println("ServiceHost exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRmi() {
        return rmi;
    }

    public void setRmi(String rmi) {
        this.rmi = rmi;
    }
}
