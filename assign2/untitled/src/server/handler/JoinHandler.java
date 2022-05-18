package server.handler;

import server.Node;

import java.net.Socket;

public class JoinHandler implements Runnable{

    private final Socket membershipSocket;
    private final Node node;

    public JoinHandler(Socket membershipSocket, Node node){
        this.membershipSocket = membershipSocket;
        this.node = node;
    }


    @Override
    public void run() {
        
    }
}
