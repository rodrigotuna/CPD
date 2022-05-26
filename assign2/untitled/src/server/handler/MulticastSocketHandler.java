package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.LeaveMessage;
import server.message.UDPMessage;
import server.message.MessageParser;

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
                Thread thread = null;
                switch(message.getType()){
                    case "MEMBERSHIP":
                        break;
                    case "JOIN":
                        thread = new Thread(new JoinMessageHandler((JoinMessage) message, node));
                        thread.start();
                        break;
                    case "LEAVE":
                        thread = new Thread(new LeaveMessageHandler((LeaveMessage) message, node));
                        thread.start();
                        break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
