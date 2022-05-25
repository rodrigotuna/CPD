package server.message;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public abstract class UDPMessage {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String NL = "" + CR + LF;

    private final String type;
    private final InetSocketAddress socketAddress;
    private final String senderId;


    public UDPMessage(String type, String senderId, InetSocketAddress socketAddress) {
        this.type = type;
        this.socketAddress = socketAddress;
        this.senderId = senderId;
    }

    public String getHeader(){
        return type + " " + senderId;
    }

    public String getBody(){
        return "";
    }
    public DatagramPacket getDatagram(){
        byte[] buffer = (getHeader() + NL + NL + getBody()).getBytes();
        return new DatagramPacket(buffer,buffer.length, socketAddress);
    }

    public String getType() {
        return type;
    }
}
