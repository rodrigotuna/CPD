package server.handler;

import server.Node;
import server.storage.MembershipLog;

import java.net.Socket;

public class TCPHandler implements Runnable{

    private final Socket nodeSocket;
    private final Node node;

    public TCPHandler(Socket nodeSocket, Node node){
        this.nodeSocket = nodeSocket;
        this.node = node;

    }


    @Override
    public void run() {
        while(true){

        }
    }
}
