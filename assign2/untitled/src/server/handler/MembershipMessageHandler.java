package server.handler;

import server.Node;
import server.message.MembershipMessage;

public class MembershipMessageHandler implements Runnable{
    private final MembershipMessage membershipMessage;
    private final Node node;

    public MembershipMessageHandler(MembershipMessage membershipMessage, Node node){
        this.membershipMessage = membershipMessage;
        this.node = node;
    }


    @Override
    public void run() {

    }
}
