package server.handler;

import server.Node;
import server.message.Message;
import server.message.MessageHandler;
import server.message.MessageParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastHandler implements Runnable{

    private final MulticastSocket membershipSocket;
    private final Node node;

    public MulticastHandler(MulticastSocket membershipSocket, Node node){
        this.membershipSocket = membershipSocket;
        this.node = node;
    }
    @Override
    public void run() {

        byte[] buffer = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        MessageParser messageParser = new MessageParser();
        MessageHandler messageHandler = new MessageHandler();

        while(true){
            try {
                membershipSocket.receive(packet);
                //TODO
                Message message = messageParser.parse(packet);
                //messageHandler.handle(message, node);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
