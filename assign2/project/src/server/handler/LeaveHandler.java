package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.LeaveMessage;
import server.storage.Ring;

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
                    node.getMembershipAddress(), membershipCounter, node.getAccessPoint()).getDatagram());

            node.getTcpSocketHandler().stop();
            node.getRing().removeMember(node.getHashId());
            // TODO: Não sei se está a apagar direito
            for(int i = 0; i < Ring.REPLICATION_FACTOR; i++){
                String recipient = node.getRing().getResponsible(node.getHashId()).get(i);
                List<File> files = node.getFileSystem().getFiles(i);
                for(File file : files){
                    node.executeThread(new FileTransferHandler(file, recipient, node, i, true));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
