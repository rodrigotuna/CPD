package server.handler;

import server.Node;
import server.message.JoinMessage;
import server.message.TCPMembershipMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

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
            node.getMembershipLog().addEntry(joinMessage.getSenderId(),
                                            joinMessage.getMembershipCounter());
            node.getRing().addMember(joinMessage.getSenderId(), joinMessage.getAccessPoint());
            synchronized (node.getMembershipLog().getMostRecentlyUpdated()){
                if(!node.getMembershipLog().getMostRecentlyUpdated().equals(joinMessage.getSenderId())){
                    URI uri = new URI(null, joinMessage.getAccessPoint(), null, null, null);
                    Socket socket = new Socket(uri.getHost(), uri.getPort());

                    OutputStream output = socket.getOutputStream();
                    output.write(new TCPMembershipMessage(node.getHashId(),
                            node.getMembershipLog().mostRecentLogContent(), node.getRing().listMembers())
                            .getDataStringStream().getBytes());
                    output.flush(); output.close();
                    node.getMembershipLog().setMostRecentlyUpdated(joinMessage.getSenderId());
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
