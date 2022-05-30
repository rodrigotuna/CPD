package server.message;

import java.net.InetSocketAddress;

public class TCPMembershipMessage extends TCPMessage{

    public TCPMembershipMessage(String senderId , String membershipLog, String ring) {
        super("MEMBERSHIP", senderId);
        setBody(membershipLog + NL + ring);
    }

    public TCPMembershipMessage(String senderId , String body) {
        super("MEMBERSHIP", senderId);
        setBody(body);
    }
}
