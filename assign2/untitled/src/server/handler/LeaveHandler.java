package server.handler;

import server.Node;
import server.message.JoinMessage;

import java.io.IOException;

public class LeaveHandler implements Runnable{
    private final Node node;

    public LeaveHandler(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        try {
            node.getMembershipLog().incrementCounter();
            int membershipCounter = node.getMembershipLog().getMembershipCounter();
            node.stopMembershipSocket();

            node.getMembershipSocket().send(new JoinMessage(node.getHashId(),
                    node.getMembershipAddress(), membershipCounter, node.getAccessPoint()).getDatagram());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
