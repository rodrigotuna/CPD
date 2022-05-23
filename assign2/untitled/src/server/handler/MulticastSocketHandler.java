package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.Message;
import server.message.MessageParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastSocketHandler implements Runnable{

    private final MulticastSocket membershipSocket;
    private final Node node;

    public MulticastSocketHandler(MulticastSocket membershipSocket, Node node){
        this.membershipSocket = membershipSocket;
        this.node = node;
    }


    @Override
    public void run() {

        byte[] buffer = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        MessageParser messageParser = new MessageParser();

        while(true){
            try {
                membershipSocket.receive(packet);
                //TODO
                Message message = messageParser.parse(packet);
                switch(message.getType()){
                    case "MEMBERSHIP":
                        break;
                    case "JOIN":
                        new JoinMessageHandler((JoinMessage) message);
                        break;
                    case "LEAVE":
                        break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
