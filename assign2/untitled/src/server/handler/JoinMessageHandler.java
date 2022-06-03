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
            if(node.getRing().getResponsible(joinMessage.getSenderId()).get(0).equals(node.getAccessPoint())){
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
                List<File> backup2 = node.getFileSystem().getFiles(2);
                for(File file : backup2){
                    node.executeThread(new FileTransferHandler(file, joinMessage.getAccessPoint(), node, 2, true));
                }
                List<File> backup1 = node.getFileSystem().getFiles(1);
                for(File file : backup1){
                    node.executeThread(new FileTransferHandler(file, joinMessage.getAccessPoint(), node, 1, false));
                    node.getFileSystem().changeFolder(2, file);
                }
                List<File> myFiles = node.getFileSystem().getFiles(0);
                for(File file : myFiles){
                    if(node.getRing().getResponsible(file.getName()).get(0).equals(joinMessage.getAccessPoint())){
                        node.executeThread(new FileTransferHandler(file, joinMessage.getAccessPoint(), node, 0, true));
                    }
                }

            }else if(node.getRing().getResponsible(joinMessage.getSenderId()).get(1).equals(node.getAccessPoint())){
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
                List<File> backup1 = node.getFileSystem().getFiles(1);
                List<File> backup2 = node.getFileSystem().getFiles(2);
                for(File file : backup2){
                    file.delete();
                }
                for(File file : backup1){
                    if(node.getRing().getResponsible(file.getName()).get(0).equals(joinMessage.getAccessPoint())) {
                        file.delete();
                    }
                }
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
            } else if (node.getRing().getResponsible(joinMessage.getSenderId()).get(2).equals(node.getAccessPoint())){
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
                List<File> backup2 = node.getFileSystem().getFiles(2);
                for(File file : backup2){
                    if(node.getRing().getResponsible(file.getName()).get(0).equals(joinMessage.getAccessPoint())) {
                        file.delete();
                    }
                }
            }else{
                node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
            }
        }
    }
}
