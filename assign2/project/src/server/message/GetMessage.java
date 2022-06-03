package server.message;

public class GetMessage extends TCPMessage{
    public GetMessage(String key) {
        super("GET", key);
    }
}
