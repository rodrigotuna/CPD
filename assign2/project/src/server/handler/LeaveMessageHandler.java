package server.handler;

import server.Node;
import server.message.LeaveMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaveMessageHandler implements Runnable{

    private final LeaveMessage message;
    private final Node node;
    public LeaveMessageHandler(LeaveMessage message, Node node) {
        this.message = message;
        this.node = node;
    }

    @Override
    public void run() {
        try {
            node.getMembershipLog().addEntry(message.getAccessPoint(), message.getMembershipCounter());
            node.getRing().removeMember(message.getSenderId());
            if(node.getRing().getResponsible(message.getSenderId()).get(0).equals(node.getAccessPoint())){
                List<File> backup2 = node.getFileSystem().getFiles(2);
                for(File file : backup2){
                    node.executeThread(new FileTransferHandler(file, node.getRing().getResponsible(message.getSenderId()).get(1) , node, 2, true));
                }
                List<File> backup1 = node.getFileSystem().getFiles(1);
                for(File file : backup1){
                    file.delete();
                }

            }else if(node.getRing().getResponsible(message.getSenderId()).get(1).equals(node.getAccessPoint())){
                List<File> backup2 = node.getFileSystem().getFiles(2);
                for(File file : backup2){
                    file.delete();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
