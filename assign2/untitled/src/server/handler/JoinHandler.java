package server.handler;

import jdk.jshell.execution.Util;
import server.Node;
import server.message.JoinMessage;
import server.message.TCPMembershipMessage;
import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;

public class JoinHandler implements Runnable{
    private static final int NUM_TRIES = 3;
    private static final int LOGS_TO_RECEIVE = 3;
    private final Node node;
    private int numLogsReceived;

    private String[] logsReceived = new String[LOGS_TO_RECEIVE];


    public JoinHandler(Node node){
        numLogsReceived = 0;
        this.node = node;
    }
    @Override
    public void run() {
        try {
            node.getMembershipLog().incrementCounter();
            int membershipCounter = node.getMembershipLog().getMembershipCounter();
            node.getRing().addMember(node.getHashId(), node.getAccessPoint());
            node.StartTCPMembershipSocket();
            for(int i = 0; i < NUM_TRIES; i++){
                node.getMembershipSocket().send(new JoinMessage(node.getHashId(),
                        node.getMembershipAddress(), membershipCounter, node.getAccessPoint()).getDatagram());

                while(numLogsReceived< LOGS_TO_RECEIVE){
                    TCPMembershipMessage membershipMessage = node.getTcpMembershipSocketHandler().getMembershipMessage();

                    if(membershipMessage == null){
                        System.out.println("Not enough messages retrying...");
                        break;
                    }else{
                        logsReceived[numLogsReceived++] = membershipMessage.getBody();
                    }
                }
            }
            node.getTcpMembershipSocketHandler().stop();
            if(numLogsReceived == 0) node.scheduleThread(new PeriodicMembership(node), 1000);
            for(int i = 0; i < numLogsReceived; i++){
                int index = Utils.indexOf(logsReceived[i].getBytes(), "\r\n".getBytes());
                node.getMembershipLog().mergeLog(logsReceived[i].substring(0,index));
                node.getRing().mergeRing(logsReceived[i].substring(index+2));
            }
            node.setMembershipRunning(false);
            node.StartMembershipSocket();
            node.StartTCPSocket();
            node.scheduleThread(new PeriodicMembershipRecovery(node),4000);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
