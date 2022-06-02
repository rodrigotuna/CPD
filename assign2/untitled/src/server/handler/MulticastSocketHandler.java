package server.handler;

import server.Node;
import server.message.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastSocketHandler implements Runnable{

    private final MulticastSocket membershipSocket;
    private final Node node;
    private boolean running;

    public MulticastSocketHandler(MulticastSocket membershipSocket, Node node){
        this.membershipSocket = membershipSocket;
        this.node = node;
        this.running = true;
    }


    @Override
    public void run() {

        byte[] buffer = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        MessageParser messageParser = new MessageParser();

        while(running){
            try {
                membershipSocket.receive(packet);
                UDPMessage message = messageParser.parse(packet);
                switch(message.getType()){
                    case "MEMBERSHIP":
                        node.executeThread(new MembershipMessageHandler((UDPMembershipMessage) message, node));
                        break;
                    case "JOIN":
                        node.executeThread(new JoinMessageHandler((JoinMessage) message, node));
                        break;
                    case "LEAVE":
                        node.executeThread(new LeaveMessageHandler((LeaveMessage) message, node));
                        break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
