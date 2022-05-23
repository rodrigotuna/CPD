package server.message;

import java.net.InetSocketAddress;

public class MembershipMessage extends Message{

    public MembershipMessage(String type, String senderId, InetSocketAddress socketAddress) {
        super(type, senderId, socketAddress);
    }
}
