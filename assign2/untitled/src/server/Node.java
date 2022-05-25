package server;

import server.handler.JoinHandler;
import server.handler.MulticastSocketHandler;
import server.handler.TCPSocketHandler;
import server.state.JoinState;
import server.storage.MembershipLog;
import utils.Utils;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Node implements MembershipInterface {

    private final String hashId;
    private final InetSocketAddress membershipAddress;
    private final MulticastSocket membershipSocket;
    private final InetSocketAddress accessPoint;

    private final MembershipLog membershipLog;

    private MulticastSocketHandler multicastSocketHandler;
    private TCPSocketHandler tcpSocketHandler;

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException, NoSuchAlgorithmException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;
        this.hashId = Utils.bytesToHexString(Utils.hash256(getAccessPoint().getBytes()));

        this.membershipLog = new MembershipLog(hashId);

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);
    }

    public void join(){
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
        this.multicastSocketHandler = multicastHandler;
        Thread multicastThread = new Thread(multicastHandler);
        multicastThread.start();
    }
    public void StartTCPSocket() throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(accessPoint);
        TCPSocketHandler tcpHandler = new TCPSocketHandler(socket, this);
        this.tcpSocketHandler = tcpHandler;
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

    public MulticastSocketHandler getMulticastSocketHandler() {
        return multicastSocketHandler;
    }

    public TCPSocketHandler getTcpSocketHandler() {
        return tcpSocketHandler;
    }

    public String getHashId(){
        return hashId;
    }

    public MembershipLog getMembershipLog() {
        return membershipLog;
    }
}
