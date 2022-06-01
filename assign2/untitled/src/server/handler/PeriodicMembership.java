package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.UDPMembershipMessage;

import java.io.IOException;

public class PeriodicMembership implements Runnable{
    private final Node node;

    PeriodicMembership(Node node){
        this.node = node;
    }
    @Override
    public void run() {
        try {
            node.getMembershipSocket().send(new UDPMembershipMessage(node.getHashId(), node.getMembershipAddress()).getDatagram());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
