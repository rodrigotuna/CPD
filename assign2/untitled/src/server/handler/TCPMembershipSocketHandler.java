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

public class TCPMembershipSocketHandler implements Runnable {

    private final ServerSocket nodeSocket;
    private final Node node;

    private final BlockingQueue<TCPMembershipMessage> membershipMessages = new LinkedBlockingQueue<>();

    public TCPMembershipSocketHandler(ServerSocket nodeSocket, Node node) {
        this.nodeSocket = nodeSocket;
        this.node = node;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            while (true) {
                socket = nodeSocket.accept();
                node.executeThread(new TCPMessageHandler(socket));
            }
        } catch (IOException e) {
            System.out.println("Closing connections on membership socket");
        }
    }

    public void stop() throws IOException {
        nodeSocket.close();
    }

    TCPMembershipMessage getMembershipMessage() throws InterruptedException {
        return membershipMessages.poll(1000, TimeUnit.MILLISECONDS);
    }

    class TCPMessageHandler implements Runnable {
        private final Socket socket;

        private TCPMessageHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream input = socket.getInputStream();
                byte[] data = input.readAllBytes();
                TCPMessage message = new MessageParser().parse(data);
                membershipMessages.put((TCPMembershipMessage) message);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
