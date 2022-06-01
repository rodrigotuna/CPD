package server.message;

import java.net.InetSocketAddress;

public class UDPMembershipMessage extends UDPMessage{

    public UDPMembershipMessage(String senderId, InetSocketAddress socketAddress) {
        super("MEMBERSHIP", senderId, socketAddress);
    }
}
