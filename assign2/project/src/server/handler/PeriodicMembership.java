package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.UDPMembershipMessage;

import java.io.IOException;

public class PeriodicMembership implements Runnable{
    private final Node node;
    private final int odd;

    PeriodicMembership(Node node, int odd){
        this.node = node;
        this.odd = odd;
    }
    @Override
    public void run() {
        try {
            String body = Math.random() < Math.exp(-odd)
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
