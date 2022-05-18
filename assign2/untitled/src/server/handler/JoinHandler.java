package server.handler;

import server.Node;
import server.message.JoinMessage;

import java.io.IOException;

public class JoinHandler implements Runnable{
    private static final int NUM_TRIES = 3;
    private static final int MAX_LOG_RECEIVED = 3;
    private final Node node;
    private int logsReceived;


    public JoinHandler(Node node){
        logsReceived = 0;
        this.node = node;
    }
    @Override
    public void run() {
        try {
            node.StartTCPSocket();
            for(int i = 0; i < NUM_TRIES; i++){
                node.getMembershipSocket().send(new JoinMessage(node.getAccessPoint(),
                        node.getMembershipAddress()).getDatagram());

                int tries = 0;
                while(logsReceived < 3 && tries < 3){
                    //wait(1000,0);
                    tries++;
                }

                if(logsReceived == MAX_LOG_RECEIVED){
                    break;
                }else{
                    System.out.println("Not enough logs, retrying");
                }
            }
            node.StartMembershipSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
