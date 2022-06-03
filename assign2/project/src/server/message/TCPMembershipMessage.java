package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class TCPMembershipMessage extends TCPMessage{

    public TCPMembershipMessage(String senderId , String membershipLog, String ring) {
        super("MEMBERSHIP", senderId);
        setBody(membershipLog + "\r\n" + ring);
    }

    public TCPMembershipMessage(String senderId , String body) {
        super("MEMBERSHIP", senderId);
        setBody(body);
    }

    public TCPMembershipMessage(String senderId) {
        super("MEMBERSHIP", senderId);
    }

    public String getDataStringStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((getHeader() + "\r\n\r\n" ).getBytes());
        if(this.body != null) buffer.write(this.body.getBytes());

        return buffer.toString();
    }
}
