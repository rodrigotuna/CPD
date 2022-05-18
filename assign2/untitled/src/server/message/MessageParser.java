package server.message;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class MessageParser {
    public Message parse(DatagramPacket packet){
        byte [] data = packet.getData();
        InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
        return null;
    }

    public Message parse(InputStream inputStream) throws IOException {
        byte [] data = inputStream.readAllBytes();
        return null;
    }
}
