package server.handler;

import server.Node;
import server.message.TCPMembershipMessage;
import server.message.TCPMessage;
import server.message.MessageParser;
import utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
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
        Socket socket;
        try{
            while (true) {
                socket = nodeSocket.accept();
                executor.execute(new TCPMessageHandler(socket));
            }
        }
        catch (IOException e) {
            System.out.println("Thread vai acabar");
        }
    }

    public void stop() throws IOException {
        nodeSocket.close();
    }

    TCPMembershipMessage getMembershipMessage() throws InterruptedException {
        return membershipMessages.poll(1000, TimeUnit.MILLISECONDS);
    }

    public void putValue(String nodeAccessPoint, String key, String fileContent) {
        try {
            File file = new File("filesystem/" + nodeAccessPoint + "/" + key + ".file");
            if (!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();

            FileWriter valueWriter = new FileWriter(file);
            valueWriter.write(fileContent);

            valueWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteValue(String nodeAccessPoint, String key) {
        try {
            File file = new File("filesystem/" + nodeAccessPoint + "/" + key + ".file");
            if (!file.exists()) throw new FileNotFoundException();

            if(!file.delete()) throw new IOException();

            // IS IT NECESSARY TO DELETE NODE FOLDER?
            if(file.getParentFile().isDirectory() && Objects.requireNonNull(file.getParentFile().list()).length == 0)
                if(!file.getParentFile().delete()) throw new IOException();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class TCPMessageHandler implements Runnable{
        private final Socket socket;

        private TCPMessageHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                InputStream input = socket.getInputStream();
                byte[] data = input.readAllBytes();
                TCPMessage message = new MessageParser().parse(data);
                switch (message.getType()) {
                    case "PUT":
                        putValue(node.getAccessPoint(),message.getKey(),message.getBody());
                        // SEND OK MESSAGE?
                        break;
                    case "GET":
                        break;
                    case "DELETE":
                        deleteValue(node.getAccessPoint(), message.getKey());
                        // SEND OK MESSAGE?
                        break;
                    case "MEMBERSHIP":
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
