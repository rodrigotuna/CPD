package server.handler;

import server.Node;
import server.message.TCPMembershipMessage;

public class MembershipMessageHandler implements Runnable{
    private final TCPMembershipMessage membershipMessage;
    private final Node node;

    public MembershipMessageHandler(TCPMembershipMessage membershipMessage, Node node){
        this.membershipMessage = membershipMessage;
        this.node = node;
    }


    @Override
    public void run() {

    }
}
