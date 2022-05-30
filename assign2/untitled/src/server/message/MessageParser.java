package server.message;

import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static utils.Utils.bytesToHexString;
import static utils.Utils.bytesToString;

public class MessageParser {
    public UDPMessage parse(DatagramPacket packet){
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
                return new LeaveMessage(senderId, socketAddress, Integer.parseInt(headerFields[2]));
        }
        return null;
    }

    public TCPMessage parse(byte[] data) throws IOException {
        int headerSize = Utils.indexOf(data, "\r\n\r\n".getBytes());
        String header = new String(Arrays.copyOfRange(data, 0, headerSize));
        String [] headerFields =header.split("[ ]+");
        String type = headerFields[0];
        String key = headerFields[1];
        switch(type){
            case "MEMBERSHIP":
                return new TCPMembershipMessage(key,
                        bytesToString(Arrays.copyOfRange(data, headerSize + 4, data.length)));
            case "PUT":
                return new PutMessage(key,
                        bytesToString((Arrays.copyOfRange(data, headerSize + 4, data.length))));
            case "DELETE":
                break;
            case "GET":
                break;
        }
        return null;
    }
}
