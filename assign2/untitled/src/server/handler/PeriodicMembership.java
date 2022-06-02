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
            String body = Math.random() > Math.exp(-node.getMembershipLog().getPenalty())
                    ? node.getMembershipLog().mostRecentLogContent() : "";
            node.getMembershipSocket().send(new UDPMembershipMessage(node.getHashId(),
                                                                     node.getMembershipAddress(),
                                                                     node.getRing().getNext(node.getHashId()),
                                                                     body).getDatagram());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
