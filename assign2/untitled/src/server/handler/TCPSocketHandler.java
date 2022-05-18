package server.handler;

import server.Node;

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
        while(true){

        }
    }
}
