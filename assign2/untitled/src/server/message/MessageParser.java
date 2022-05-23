package server.message;

import server.handler.JoinMessageHandler;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageParser {
    public Message parse(DatagramPacket packet){
        byte [] data = packet.getData();
        InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
        int headerSize = Utils.indexOf(data, "\r\n\r\n".getBytes());
        String header = new String(Arrays.copyOf(data, headerSize));
        String [] headerFields =header.split("[ ]+");
        String type = headerFields[0];
        String senderId = headerFields[1];
        switch(type){
            case "MEMBERSHIP":
                break;
            case "JOIN":
                return new JoinMessage(senderId, socketAddress, Integer.parseInt(headerFields[2]), headerFields[3]);
            case "LEAVE":
                break;
        }
        return null;
    }

    public Message parse(InputStream inputStream) throws IOException {
        byte [] data = inputStream.readAllBytes();
        return null;
    }
}
