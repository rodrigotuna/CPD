package server.message;

import java.net.InetSocketAddress;

public class JoinMessage extends UDPMessage {
    private final int membershipCounter;
    private final String accessPoint;

    public JoinMessage(String senderId, InetSocketAddress socketAddress, int membershipCounter, String accessPoint) {
        super("JOIN", senderId, socketAddress);
        this.membershipCounter = membershipCounter;
        this.accessPoint = accessPoint;
    }

    public String getHeader(){
        return super.getHeader() + " " + membershipCounter + " " + accessPoint;
    }

    public int getMembershipCounter(){
        return membershipCounter;
    }

    public String getAccessPoint() {
        return accessPoint;
    }
}
