package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMessage {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String NL = CR + LF + "\n";

    private final String key;
    private final byte[] value;

    public PutMessage(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public String getHeader(){
        return "PUT " + key;
    }

    public byte[] getDataByteStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((getHeader() + NL + NL ).getBytes());
        buffer.write(this.value);

        return buffer.toByteArray();
    }
}
