package server.message;

import java.net.InetSocketAddress;

public class UDPMembershipMessage extends UDPMessage{
    private final String nextId;
    private final String body;
    public UDPMembershipMessage(String senderId, InetSocketAddress socketAddress, String nextId, String body) {
        super("MEMBERSHIP", senderId, socketAddress);
        this.nextId = nextId;
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

    public String getNextId() {
        return nextId;
    }
}
