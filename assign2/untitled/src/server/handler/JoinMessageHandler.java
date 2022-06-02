package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.TCPMembershipMessage;

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
            if(node.getRing().getResponsible(joinMessage.getSenderId()).get(0).equals(node.getAccessPoint())){
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());

                List<File> prevFiles = node.getFileSystem().getFiles();
                for(File file : prevFiles){
                    if(node.getRing().getResponsible(file.getName()).get(0).equals(joinMessage.getAccessPoint())){
                        node.executeThread(new FileTransferHandler(file, joinMessage.getAccessPoint(), node, 0, true));
                    }
                }
            }else{
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
            }
        }
    }
}
