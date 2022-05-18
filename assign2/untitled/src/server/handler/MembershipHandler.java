package server.handler;

import server.Node;
import server.message.Message;
import server.message.MessageParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MembershipHandler implements Runnable{

    private final MulticastSocket membershipSocket;
    private final Node node;

    public MembershipHandler(MulticastSocket membershipSocket, Node node){
        this.membershipSocket = membershipSocket;
        this.node = node;
    }
    @Override
    public void run() {
        byte[] buffer = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(true){
            try {
                membershipSocket.receive(packet);
                Message message = new MessageParser().parse(packet);
                System.out.println(new String(packet.getData()).trim());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
