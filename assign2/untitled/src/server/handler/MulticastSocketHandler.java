package server.handler;

import server.Node;
import server.message.Message;
import server.message.MessageHandler;
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
        MessageHandler messageHandler = new MessageHandler();

        while(true){
            try {
                membershipSocket.receive(packet);
                //TODO
                Message message = messageParser.parse(packet);
                switch(message.getType()){
                    case "MEMBERSHIP":
                        //Acho que é a única, isto é parvo mas no TCP já faz sentido;
                        // so nao sei receber msgs por TCP;
                    case "JOIN":

                    case "LEAVE":
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
