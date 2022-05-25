package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class TCPMessage {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String NL = CR + LF + "\n";

    private final String type;
    private final String key;
    private final byte[] value;

    public TCPMessage(String type, String key, byte[] value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getHeader(){
        return type + key;
    }

    public byte[] getDataByteStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((getHeader() + NL + NL ).getBytes());
        buffer.write(this.value);

        return buffer.toByteArray();
    }
}
