package server.handler;

import server.Node;
import server.message.MembershipMessage;
import server.message.Message;
import server.message.MessageParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TCPSocketHandler implements Runnable{

    private final ServerSocket nodeSocket;
    private final Node node;

    private final BlockingQueue<MembershipMessage> membershipMessages = new LinkedBlockingQueue<>();

    public TCPSocketHandler(ServerSocket nodeSocket, Node node){
        this.nodeSocket = nodeSocket;
        this.node = node;

    }

    @Override
    public void run() {
        MessageParser messageParser = new MessageParser();
        while(true){
            try {
                Socket socket = nodeSocket.accept();
                InputStream input = socket.getInputStream();
                Message message = messageParser.parse(input);

                switch(message.getType()){
                    case "PUT":
                        break;
                    case "GET":
                        break;
                    case "DELETE":
                        break;
                    case "MEMBERSHIP":
                        membershipMessages.put((MembershipMessage) message);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + message.getType());
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    MembershipMessage getMembershipMessage() throws InterruptedException {
        return membershipMessages.poll(1000, TimeUnit.MILLISECONDS);
    }

}
