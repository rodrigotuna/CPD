package server;

import server.handler.*;
import server.storage.FileSystem;
import server.storage.MembershipLog;
import server.storage.Ring;
import utils.Utils;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Node implements MembershipInterface {

    private final String hashId;
    private Boolean membershipRunning;

    private State state;
    private final InetSocketAddress membershipAddress;
    private final MulticastSocket membershipSocket;
    private final InetSocketAddress accessPoint;

    private final MembershipLog membershipLog;
    private final Ring ring;
    private final FileSystem fileSystem;

    private final MulticastSocketHandler multicastSocketHandler;
    private TCPSocketHandler tcpSocketHandler;
    private TCPMembershipSocketHandler tcpMembershipSocketHandler;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public Node(InetSocketAddress membershipAddress, InetSocketAddress accessPoint) throws IOException, AlreadyBoundException, NoSuchAlgorithmException {

        this.membershipAddress = membershipAddress;
        this.membershipSocket = new MulticastSocket(membershipAddress.getPort());

        this.accessPoint = accessPoint;
        this.hashId = Utils.bytesToHexString(Utils.hash256(getAccessPoint().getBytes()));
        this.state = State.OUT;

        this.membershipLog = new MembershipLog(getAccessPoint());
        this.ring = new Ring();
        addChanges(new ArrayList<>(), membershipLog.nodesJoined());
        this.fileSystem = new FileSystem(getAccessPoint(), hashId);

        MembershipInterface stub = (MembershipInterface) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.bind(getAccessPoint(), stub);

        MulticastSocketHandler multicastHandler = new MulticastSocketHandler(membershipSocket, this);
        this.multicastSocketHandler = multicastHandler;
        Thread multicastThread = new Thread(multicastHandler);
        multicastThread.start();

        if(membershipLog.getMembershipCounter() % 2 == 0){
            startMembershipSocket();
            startTCPSocket();
            scheduleThread(new PeriodicMembershipRecovery(this), Math.max(4, ring.numClients())*1000);
        }
    }

    public void join() {
        if (state == State.OUT) {
            JoinHandler joinHandler = new JoinHandler(this);
            Thread joinThread = new Thread(joinHandler);
            joinThread.start();
        }
    }

    public void leave() {
        if (state == State.IN) {
            LeaveHandler leaveHandler = new LeaveHandler(this);
            Thread leaveThread = new Thread(leaveHandler);
            leaveThread.start();
        }
    }

    public String getAccessPoint() {
        return accessPoint.toString().substring(1);
    }

    public void startMembershipSocket() throws IOException {
        state = State.IN;
        membershipSocket.joinGroup(membershipAddress.getAddress());
    }

    public void startTCPMembershipSocket() throws IOException, URISyntaxException {
        ServerSocket socket = new ServerSocket();
        URI uri = new URI(null, getAccessPoint(), null, null, null);
        socket.bind(new InetSocketAddress(uri.getHost(), 8888));
        TCPMembershipSocketHandler tcpHandler = new TCPMembershipSocketHandler(socket, this);
        this.tcpMembershipSocketHandler = tcpHandler;
        Thread tcpThread = new Thread(tcpHandler);
        tcpThread.start();
    }

    public void startTCPSocket() throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(accessPoint);
        TCPSocketHandler tcpHandler = new TCPSocketHandler(socket, this);
        this.tcpSocketHandler = tcpHandler;
        Thread tcpThread = new Thread(tcpHandler);
        tcpThread.start();
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

    public String getHashId() {
        return hashId;
    }

    public MembershipLog getMembershipLog() {
        return membershipLog;
    }

    public void stopMembershipSocket() throws IOException {
        state = State.OUT;
        membershipSocket.leaveGroup(membershipAddress.getAddress());
    }

    public Ring getRing() {
        return ring;
    }


    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void executeThread(Runnable runnable) {
        executor.execute(runnable);
    }

    public void scheduleThread(Runnable runnable, int millis) {
        executor.schedule(runnable, millis, TimeUnit.MILLISECONDS);
    }

    public boolean membershipRunning() {
        return membershipRunning;
    }

    public void setMembershipRunning(Boolean membershipRunning) {
        this.membershipRunning = membershipRunning;
    }

    public State getState() {
        return state;
    }

    public void addChanges(List<String> left, List<String> join) {
        for(String s : left){
            ring.removeMember(Utils.bytesToHexString(Utils.hash256(s.getBytes())));
        }

        for(String s : join){
            ring.addMember(Utils.bytesToHexString(Utils.hash256(s.getBytes())), s);
        }
    }

}
