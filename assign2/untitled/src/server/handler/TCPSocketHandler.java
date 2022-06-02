
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class TCPSocketHandler implements Runnable{

    private final ServerSocket nodeSocket;
    private final Node node;

    private final BlockingQueue<byte []> membershipMessages = new LinkedBlockingQueue<>();

    public TCPSocketHandler(ServerSocket nodeSocket, Node node){
        this.nodeSocket = nodeSocket;
        this.node = node;
    }

    @Override
    public void run() {
        try{
            while (true) {
                node.executeThread(new TCPMessageHandler(nodeSocket.accept()));
            }
        }
        catch (IOException e) {

        }
    }

    public void stop() throws IOException {
        nodeSocket.close();
    }

    byte [] getMembershipMessage() throws InterruptedException {
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
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String header = bufferedReader.readLine();
                System.out.println(header);
                System.out.println(inputStream);
                TCPMessage message = new MessageParser().parseHeader(header);

                OutputStream outputStream = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(outputStream, true);
                List<String> responsibleAccessPoints = node.getRing().getResponsible(message.getKey());

                switch (message.getType()) {
                    case "PUT":
                        if(responsibleAccessPoints.get(0).equals(node.getAccessPoint())){
                            pw.println(200);
                            byte [] data = inputStream.readAllBytes();
                            node.getFileSystem().put(message.getKey(), new String(data));
                        }else {
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(0));
                        }
                        break;
                    case "GET":
                        if(responsibleAccessPoints.get(0).equals(node.getAccessPoint())){
                            pw.println(200);
                            pw.println(node.getFileSystem().get(message.getKey()));
                            pw.flush();
                            socket.close();
                        }else{
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(0));
                        }
                        break;
                    case "DELETE":
                        if(responsibleAccessPoints.get(0).equals(node.getAccessPoint())){
                            pw.println(200);
                            node.getFileSystem().delete(message.getKey());
                        }else{
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(0));
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + message.getType());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}