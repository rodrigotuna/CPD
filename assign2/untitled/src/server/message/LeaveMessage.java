package server.message;

import server.handler.LeaveHandler;

import java.net.InetSocketAddress;

public class LeaveMessage extends UDPMessage{
    private final int membershipCounter;

    public LeaveMessage(String senderId, InetSocketAddress socketAddress, int membershipCounter) {
        super("LEAVE", senderId, socketAddress);
        this.membershipCounter = membershipCounter;
    }

    public String getHeader(){
        return super.getHeader() + " " + membershipCounter;
    }

    public int getMembershipCounter(){
        return membershipCounter;
    }
}
