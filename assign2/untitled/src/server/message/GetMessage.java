package server.message;

public class GetMessage extends TCPMessage{
    public GetMessage(int factor, String key) {
        super("GET", key);
    }
}
