package server.message;

import java.net.DatagramPacket;

public class Message {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String CRLF  = "" + CR + LF;
    private final static String NL = CRLF + "\n";

    public DatagramPacket getDatagram(){
        //return new this.getBytes()
        return null;
    }
}
