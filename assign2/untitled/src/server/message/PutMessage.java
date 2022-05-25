package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMessage extends TCPMessage{
    public PutMessage(String key, String body) {
        super("PUT", key);
        setBody(body);
    }
}
