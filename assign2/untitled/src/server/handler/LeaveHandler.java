package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.LeaveMessage;

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

            node.getMembershipSocket().send(new LeaveMessage(node.getHashId(),
                    node.getMembershipAddress(), membershipCounter).getDatagram());

            //TODO Mandar os ficheiros que tenho a quem devo mandar
            node.getTcpSocketHandler().stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
