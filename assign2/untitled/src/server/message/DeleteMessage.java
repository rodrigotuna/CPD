package server.message;

public class DeleteMessage extends TCPMessage{
    public DeleteMessage(String type, String key) {
        super(type, key);
    }
}
