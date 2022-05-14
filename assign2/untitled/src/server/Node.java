package server;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Node implements MembershipInterface {

    private final InetSocketAddress membershipAddress;
    private final MulticastSocket membershipSocket;
    private final String accessPoint;
    private final int port;

    private int membershipCounter;
    public Node(InetSocketAddress membershipAddress, String accessPoint, int port) throws IOException, AlreadyBoundException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        membershipSocket.joinGroup(membershipAddress.getAddress());

        this.accessPoint = accessPoint;
        this.port = port;

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(accessPoint, stub);
    }

    public void join() throws IOException {
        membershipSocket.send(new DatagramPacket("hahahaha".getBytes(StandardCharsets.UTF_8),8));
    }

    public void leave() throws RemoteException{

    }


    //Not sure if it is like this
    public String put(File file) throws RemoteException {
        return "";
    }

    public void get(String hashcode) throws RemoteException {
    }

    public void delete(String hashcode) throws RemoteException{
    }

}
