package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.LeaveMessage;

import java.io.IOException;

public class LeaveMessageHandler implements Runnable{

    private final LeaveMessage message;
    private final Node node;
    public LeaveMessageHandler(LeaveMessage message, Node node) {
        this.message = message;
        this.node = node;
    }

    @Override
    public void run() {
        try {
            node.getMembershipLog().addEntry(message.getSenderId(), message.getMembershipCounter());
            node.getRing().removeMember(message.getSenderId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
