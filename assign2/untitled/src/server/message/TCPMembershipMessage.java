package server.message;

import java.net.InetSocketAddress;

public class TCPMembershipMessage extends TCPMessage{

    public TCPMembershipMessage(String senderId , byte[] body) {
        super("MEMBERSHIP", senderId, body);
    }
}
