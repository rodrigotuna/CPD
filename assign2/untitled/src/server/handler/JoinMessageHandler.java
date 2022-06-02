package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.TCPMembershipMessage;
import server.storage.Ring;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class JoinMessageHandler implements Runnable{
    private final JoinMessage joinMessage;
    private final Node node;

    public JoinMessageHandler(JoinMessage joinMessage, Node node) {
        this.joinMessage = joinMessage;
        this.node = node;
    }


    @Override
    public void run() {
        try {
            node.getMembershipLog().addEntry(joinMessage.getAccessPoint(),
                    joinMessage.getMembershipCounter());
            synchronized (node.getMembershipLog().getMostRecentlyUpdated()){
                if(!node.getMembershipLog().getMostRecentlyUpdated().equals(joinMessage.getSenderId())){
                    URI uri = new URI(null, joinMessage.getAccessPoint(), null, null, null);
                    Thread.sleep(node.getMembershipLog().getPenalty());
                    Socket socket = new Socket(uri.getHost(), 8888);

                    OutputStream output = socket.getOutputStream();
                    output.write(new TCPMembershipMessage(node.getHashId(),
                            node.getMembershipLog().mostRecentLogContent(), node.getRing().listMembers())
                            .getDataStringStream().getBytes());
                    output.flush(); output.close();
                    node.getMembershipLog().setMostRecentlyUpdated(joinMessage.getSenderId());
                }
            }

        } catch (IOException | URISyntaxException | InterruptedException ignored) {

        } finally {
            System.out.println("RESPONSIBLE SENDER: " + node.getRing().getResponsible(joinMessage.getSenderId()));
            System.out.println("ACCESS POINT: " + node.getAccessPoint());
            if(node.getRing().getResponsible(joinMessage.getSenderId()).get(0).equals(node.getAccessPoint())){
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
                for(int i = 0; i < Ring.REPLICATION_FACTOR; i++){
                    List<File> prevFiles = node.getFileSystem().getFiles(i);
                    for(File file : prevFiles){
                        // TODO: Não sei se está a apagar direito
                        if(node.getRing().getResponsible(file.getName()).get(i).equals(joinMessage.getAccessPoint())){
                            node.executeThread(new FileTransferHandler(file, joinMessage.getAccessPoint(), node, i, true));
                        }
                    }
                }

            }else{
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
            }
        }
    }
}
