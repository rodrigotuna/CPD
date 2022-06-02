package server.handler;

import server.Node;
import server.message.TCPMembershipMessage;
import server.message.UDPMembershipMessage;

import java.io.IOException;

public class MembershipMessageHandler implements Runnable{
    private final UDPMembershipMessage membershipMessage;
    private final Node node;

    public MembershipMessageHandler(UDPMembershipMessage membershipMessage, Node node){
        this.membershipMessage = membershipMessage;
        this.node = node;
    }


    @Override
    public void run() {
        try {
            node.setMembershipRunning(true);
            if(membershipMessage.getNextId().equals(node.getHashId())){
                node.scheduleThread(new PeriodicMembership(node), 1000);
            }
            if(!membershipMessage.getBody().equals("")){
                node.getMembershipLog().mergeLog(membershipMessage.getBody());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
