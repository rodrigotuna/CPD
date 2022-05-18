package server;

import server.handler.MembershipHandler;
import server.message.JoinMessage;
import server.message.Message;

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
    private final InetSocketAddress accessPoint;
    private final MulticastSocket membershipSocket;

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);
    }

    public void join() throws IOException {
        membershipSocket.send(new JoinMessage("JOIN" , getAccessPoint(),  membershipAddress).getDatagram());

        membershipSocket.joinGroup(membershipAddress.getAddress());
        MembershipHandler membershipHandler = new MembershipHandler(membershipSocket, this);
        Thread membershipThread = new Thread(membershipHandler);
        membershipThread.start();


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

    public String getAccessPoint() {
        return accessPoint.toString().substring(1);
    }
}
