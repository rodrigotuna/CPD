package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMessage extends TCPMessage{
    private final int factor;
    public PutMessage(int factor, String key) {
        super("PUT", key);
        this.factor = factor;
    }

    @Override
    public String getHeader() {
        return super.getHeader() + " " + factor;
    }

    public int getFactor() {
        return factor;
    }
}
