package server.handler;

import server.Node;
import server.message.PutMessage;
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
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                InputStreamReader ir = new InputStreamReader(inputStream);
                byte[] data = new byte[1024];
                inputStream.read(data);
                TCPMessage message = new MessageParser().parse(data);
                System.out.println(message.getType());
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(outputStream, true);
                switch (message.getType()) {
                    case "PUT":
                        String responsibleAccessPoint = node.getRing().getResponsible(message.getKey());
                        if(responsibleAccessPoint.equals(node.getAccessPoint())){
                            putValue(node.getAccessPoint(),message.getKey(),message.getBody());
                            pw.println(200);
                        }else{
                            pw.println(300);
                            pw.println(responsibleAccessPoint);
                        }
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
                System.out.println("Me voy voy voy");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
