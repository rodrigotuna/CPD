package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.TCPMembershipMessage;

import java.io.IOException;

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
            node.StartTCPSocket();
            for(int i = 0; i < NUM_TRIES; i++){
                node.getMembershipSocket().send(new JoinMessage(node.getHashId(),
                        node.getMembershipAddress(), membershipCounter, node.getAccessPoint()).getDatagram());

                while(numLogsReceived< LOGS_TO_RECEIVE){
                    TCPMembershipMessage membershipMessage = node.getTcpSocketHandler().getMembershipMessage();

                    if(membershipMessage == null){
                        System.out.println("Not enough messages retrying...");
                        break;
                    }else{
                        System.out.println(node.getHashId() + ":\n" + membershipMessage.getDataStringStream());
                        logsReceived[numLogsReceived++] = membershipMessage.getBody();
                    }
                }
            }
            node.StartMembershipSocket();
            node.getMembershipLog().mergeLog(logsReceived, numLogsReceived);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
