
package server.handler;

import server.Node;
import server.message.*;
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
                TCPMessage message = new MessageParser().parseHeader(header);

                OutputStream outputStream = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(outputStream, true);
                List<String> responsibleAccessPoints = node.getRing().getResponsible(message.getKey());

                switch (message.getType()) {
                    case "PUT":
                        PutMessage putMessage = (PutMessage) message;
                        if(responsibleAccessPoints.get(putMessage.getFactor()).equals(node.getAccessPoint())){
                            pw.println(200);
                            byte [] data = inputStream.readAllBytes();
                            File file = node.getFileSystem().put(message.getKey(), new String(data), putMessage.getFactor());

                            //REPLICATION
                            if(putMessage.getFactor() == 0){
                                System.out.println(putMessage.getFactor());
                                System.out.println("LOOOOKKKKK:" + responsibleAccessPoints.get(1));
                                node.executeThread(new FileTransferHandler(file, responsibleAccessPoints.get(1), node, 1, false));
                                node.executeThread(new FileTransferHandler(file, responsibleAccessPoints.get(2), node, 2, false));
                            }

                        }else {
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(putMessage.getFactor()));
                        }
                        break;
                    case "GET":
                        if(responsibleAccessPoints.contains(node.getAccessPoint())){
                            pw.println(200);
                            pw.println(node.getFileSystem().get(message.getKey(), responsibleAccessPoints.indexOf(node.getAccessPoint())));
                            pw.flush();
                            socket.close();
                        }else{
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(0)); // TODO: Não sei se deva ir sempre para o zero?
                        }
                        break;
                    case "DELETE":
                        DeleteMessage deleteMessage = (DeleteMessage) message;
                        if(responsibleAccessPoints.get(deleteMessage.getFactor()).equals(node.getAccessPoint())){
                            pw.println(200);
                            node.getFileSystem().delete(message.getKey(), deleteMessage.getFactor());

                            //REPLICATION
                            if(deleteMessage.getFactor() == 0){
                                node.executeThread(new DeleteFileOtherNode(message.getKey(), responsibleAccessPoints.get(1), node, 1));
                                node.executeThread(new DeleteFileOtherNode(message.getKey(), responsibleAccessPoints.get(2), node, 2));
                            }
                        }else{
                            pw.println(300);
                            pw.println(responsibleAccessPoints.get(deleteMessage.getFactor()));
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