package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMessage extends TCPMessage{
    public PutMessage(String type, String key, byte[] value) {
        super(type, key, value);
    }
}
