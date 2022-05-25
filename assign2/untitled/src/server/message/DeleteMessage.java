package server.message;

public class DeleteMessage extends TCPMessage{
    public DeleteMessage(String key) {
        super("DELETE", key);
    }
}
