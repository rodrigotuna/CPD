package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMessage extends TCPMessage{
    public PutMessage(String type, String key, String body) {
        super(type, key);
        setBody(body);
    }
}
