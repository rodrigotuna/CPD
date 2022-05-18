package server.message;

import java.net.InetSocketAddress;

public class JoinMessage extends Message{

    public JoinMessage(String type, String senderId, InetSocketAddress socketAddress) {
        super(type, senderId, socketAddress);
    }
}
