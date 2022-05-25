package server.message;

public class GetMessage extends TCPMessage{
    public GetMessage(String type, String key) {
        super(type, key);
    }
}
