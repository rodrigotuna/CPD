package server.handler;

import server.Node;
import server.State;

public class PeriodicMembershipRecovery implements Runnable{
    private final Node node;
    PeriodicMembershipRecovery(Node node){
        this.node = node;
    }
    @Override
    public void run() {
        if(node.getState() == State.OUT){
            return;
        }
        if (node.membershipRunning()) {
            node.setMembershipRunning(false);
        } else {
            if (node.getRing().isFirst(node.getHashId())) {
                node.executeThread(new PeriodicMembership(node));
            } else {

            }
        }
        node.scheduleThread(new PeriodicMembershipRecovery(node), 4000);
    }
}
