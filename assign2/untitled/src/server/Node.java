package server;

import server.handler.MulticastHandler;
import server.handler.TCPHandler;
import server.message.JoinMessage;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Node implements MembershipInterface {

    private final InetSocketAddress membershipAddress;
    private final MulticastSocket membershipSocket;
    private final InetSocketAddress accessPoint;

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);
    }

    public void join() throws IOException {
        membershipSocket.send(new JoinMessage("JOIN" , getAccessPoint(),  membershipAddress).getDatagram());;

        Socket socket = new Socket(accessPoint.getAddress(), accessPoint.getPort());
        //Thread receiveLogThread = new Thread(new TCPHandler(socket, ));


    }

    public void leave() throws IOException {
        membershipSocket.leaveGroup(membershipAddress.getAddress());

    }

    public String getAccessPoint() {
        return accessPoint.toString().substring(1);
    }

    public void StartMembershipSocket() throws IOException {
        membershipSocket.joinGroup(membershipAddress.getAddress());
        MulticastHandler membershipHandler = new MulticastHandler(membershipSocket, this);
        Thread membershipThread = new Thread(membershipHandler);
        membershipThread.start();
    }

    public void StartTCPSocket() throws IOException {
        Socket socket = new Socket(accessPoint.getAddress(), accessPoint.getPort());
    }
}
