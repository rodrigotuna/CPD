package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.LeaveMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaveHandler implements Runnable{
    private final Node node;

    public LeaveHandler(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        try {
            node.getMembershipLog().incrementCounter();
            int membershipCounter = node.getMembershipLog().getMembershipCounter();
            node.stopMembershipSocket();
            node.getMembershipSocket().send(new LeaveMessage(node.getHashId(),
                    node.getMembershipAddress(), membershipCounter).getDatagram());

            node.getTcpSocketHandler().stop();
            node.getRing().removeMember(node.getHashId());
            String recipient = node.getRing().getResponsible(node.getHashId()).get(0);
            List<File> files = node.getFileSystem().getFiles();
            for(File file : files){
                node.executeThread(new FileTransferHandler(file, recipient, node));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
