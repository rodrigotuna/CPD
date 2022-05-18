package server.message;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class MessageParser {
    public Message parse(DatagramPacket packet){
        byte [] data = packet.getData();
        InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
        return null;
    }
}
