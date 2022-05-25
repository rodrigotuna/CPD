package server.state;

import server.message.TCPMembershipMessage;

import java.util.concurrent.Callable;

public class JoinState implements Callable<TCPMembershipMessage> {
    private int logsReceived;
    private TCPMembershipMessage membershipMessage;


    @Override
    public TCPMembershipMessage call() throws Exception {
        return null;
    }
}
