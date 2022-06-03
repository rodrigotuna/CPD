package server.message;

import server.handler.LeaveHandler;

import java.net.InetSocketAddress;

public class LeaveMessage extends UDPMessage{
    private final int membershipCounter;
    private final String accessPoint;

    public LeaveMessage(String senderId, InetSocketAddress socketAddress, int membershipCounter, String accessPoint) {
        super("LEAVE", senderId, socketAddress);
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
