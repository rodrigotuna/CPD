package server.handler;

import server.Node;
import server.message.Message;
import server.message.MessageParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketHandler implements Runnable{

    private final ServerSocket nodeSocket;
    private final Node node;

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
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
