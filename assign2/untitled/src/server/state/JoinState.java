package server.state;

import server.message.MembershipMessage;

import java.util.concurrent.Callable;

public class JoinState implements Callable<MembershipMessage> {
    private int logsReceived;
    private MembershipMessage membershipMessage;


    @Override
    public MembershipMessage call() throws Exception {
        return null;
    }
}
