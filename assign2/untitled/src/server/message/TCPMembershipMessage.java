package server.message;

import java.net.InetSocketAddress;

public class TCPMembershipMessage extends TCPMessage{

    public TCPMembershipMessage(String senderId , String body) {
        super("MEMBERSHIP", senderId);
        setBody(body);
    }
}
