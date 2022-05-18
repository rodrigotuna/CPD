package server;

import server.handler.JoinHandler;
import server.handler.MulticastSocketHandler;
import server.handler.TCPSocketHandler;
import server.storage.MembershipLog;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Node implements MembershipInterface {

    private final InetSocketAddress membershipAddress;
    private final MulticastSocket membershipSocket;
    private final InetSocketAddress accessPoint;

    private MembershipLog membershipLog;

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);
    }

    public void join() throws IOException {
        JoinHandler joinHandler = new JoinHandler( this);
        Thread multicastThread = new Thread(joinHandler);
        multicastThread.start();
    }

    public void leave() throws IOException {
        membershipSocket.leaveGroup(membershipAddress.getAddress());

    }

    public String getAccessPoint() {
        return accessPoint.toString().substring(1);
    }

    public void StartMembershipSocket() throws IOException {
        membershipSocket.joinGroup(membershipAddress.getAddress());
        MulticastSocketHandler multicastHandler = new MulticastSocketHandler(membershipSocket, this);
        Thread multicastThread = new Thread(multicastHandler);
        multicastThread.start();
        // TODO thread pool
    }
    public void StartTCPSocket() throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(accessPoint);
        TCPSocketHandler tcpHandler = new TCPSocketHandler(socket, this);
        Thread tcpThread = new Thread(tcpHandler);
        tcpThread.start();
        // TODO thread pool
    }

    public MulticastSocket getMembershipSocket() {
        return membershipSocket;
    }

    public InetSocketAddress getMembershipAddress() {
        return membershipAddress;
    }
}
