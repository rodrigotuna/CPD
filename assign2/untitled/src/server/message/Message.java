package server.message;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public abstract class Message {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String NL = CR + LF + "\n";

    private final String type;
    private final InetSocketAddress socketAddress;
    private final String senderId;


    public Message(String type, String senderId, InetSocketAddress socketAddress) {
        this.type = type;
        this.socketAddress = socketAddress;
        this.senderId = senderId;
    }

    public String getHeader(){
        return type + " " + senderId;
    }
    public DatagramPacket getDatagram(){
        byte[] buffer = (getHeader() + NL + NL).getBytes();
        return new DatagramPacket(buffer,buffer.length, socketAddress);
    }

}
