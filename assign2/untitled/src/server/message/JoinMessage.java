package server.message;

import java.net.InetSocketAddress;

public class JoinMessage extends Message{

    public JoinMessage(String senderId, InetSocketAddress socketAddress) {
        super("JOIN", senderId, socketAddress);
    }
}
