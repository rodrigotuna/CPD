package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public abstract class TCPMessage {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    private final static String NL = CR + LF + "\n";

    private final String type;
    private final String key;
    private String body;

    public TCPMessage(String type, String key) {
        this.type = type;
        this.key = key;
        this.body = null;
    }

    public String getHeader(){
        return type + key;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDataByteStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((getHeader() + NL + NL ).getBytes());
        if(this.body != null) buffer.write(this.body.getBytes());

        return buffer.toString();
    }

}
