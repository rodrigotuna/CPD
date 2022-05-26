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
            node.getMembershipLog().updateFileLine(message.getSenderId(), message.getMembershipCounter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
