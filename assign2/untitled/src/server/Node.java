package server;

import server.handler.*;
import server.state.JoinState;
import server.storage.FileSystem;
import server.storage.MembershipLog;
import server.storage.Ring;
import utils.Utils;

import java.io.File;
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
    private final Ring ring;

    private final FileSystem fileSystem;

    private final MulticastSocketHandler multicastSocketHandler;
    private TCPSocketHandler tcpSocketHandler;

    private TCPMembershipSocketHandler tcpMembershipSocketHandler;

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException, NoSuchAlgorithmException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;
        this.hashId = Utils.bytesToHexString(Utils.hash256(getAccessPoint().getBytes()));

        this.membershipLog = new MembershipLog(getAccessPoint());
        this.ring = new Ring();
        this.fileSystem = new FileSystem(getAccessPoint());

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);

        MulticastSocketHandler multicastHandler = new MulticastSocketHandler(membershipSocket, this);
        this.multicastSocketHandler = multicastHandler;
        Thread multicastThread = new Thread(multicastHandler);
        multicastThread.start();
    }

    public void join(){
        JoinHandler joinHandler = new JoinHandler( this);
        Thread joinThread = new Thread(joinHandler);
        joinThread.start();
    }

    public void leave() throws IOException {
        LeaveHandler leaveHandler = new LeaveHandler(this);
        Thread leaveThread = new Thread(leaveHandler);
        leaveThread.start();
    }

    public String getAccessPoint() {
        return accessPoint.toString().substring(1);
    }

    public void StartMembershipSocket() throws IOException {
        membershipSocket.joinGroup(membershipAddress.getAddress());
    }

    public void StartTCPMembershipSocket() throws IOException, URISyntaxException {
        ServerSocket socket = new ServerSocket();
        URI uri = new URI(null, getAccessPoint(), null, null, null);
        socket.bind(new InetSocketAddress(uri.getHost(), 8888));
        TCPMembershipSocketHandler tcpHandler = new TCPMembershipSocketHandler(socket, this);
        this.tcpMembershipSocketHandler = tcpHandler;
        Thread tcpThread = new Thread(tcpHandler);
        tcpThread.start();
        // TODO thread pool
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

    public TCPMembershipSocketHandler getTcpMembershipSocketHandler() {
        return tcpMembershipSocketHandler;
    }

    public String getHashId(){
        return hashId;
    }

    public MembershipLog getMembershipLog() {
        return membershipLog;
    }

    public void stopMembershipSocket() throws IOException {
        membershipSocket.leaveGroup(membershipAddress.getAddress());
    }

    public Ring getRing() {
        return ring;
    }


    public FileSystem getFileSystem() {
        return fileSystem;
    }
}
