package server.handler;

import server.Node;
import server.message.TCPMembershipMessage;
import server.message.TCPMessage;
import server.message.MessageParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class TCPSocketHandler implements Runnable{

    private final ServerSocket nodeSocket;
    private final Node node;

    private final BlockingQueue<TCPMembershipMessage> membershipMessages = new LinkedBlockingQueue<>();

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    public TCPSocketHandler(ServerSocket nodeSocket, Node node){
        this.nodeSocket = nodeSocket;
        this.node = node;

    }

    @Override
    public void run() {
        Socket socket = null;
        while (true) {
            try{
                socket = nodeSocket.accept();
                System.out.println(socket);
                executor.execute(new TCPMessageHandler(socket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    TCPMembershipMessage getMembershipMessage() throws InterruptedException {
        return membershipMessages.poll(1000, TimeUnit.MILLISECONDS);
    }
        class TCPMessageHandler implements Runnable{
            private final Socket socket;

            private TCPMessageHandler(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {
                try{
                    System.out.println(socket);
                    InputStream input = socket.getInputStream();
                    byte[] data = input.readAllBytes();
                    TCPMessage message = new MessageParser().parse(data);
                    switch (message.getType()) {
                        case "PUT":
                            break;
                        case "GET":
                            break;
                        case "DELETE":
                            break;
                        case "MEMBERSHIP":
                            System.out.println("LA PUS HERMANO\n");
                            membershipMessages.put((TCPMembershipMessage) message);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + message.getType());
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    }

}
