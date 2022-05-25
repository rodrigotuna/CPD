package server.message;

import java.net.InetSocketAddress;

public class MembershipMessage extends UDPMessage{

    public MembershipMessage(String type, String senderId, InetSocketAddress socketAddress) {
        super(type, senderId, socketAddress);
    }
}
