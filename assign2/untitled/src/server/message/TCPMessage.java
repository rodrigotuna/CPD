package server.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class TCPMessage {
    private final static char CR  = (char) 0x0D;
    private final static char LF  = (char) 0x0A;

    protected final static String NL = "" + CR + LF;

    private final String type;
    private final String key;
    private String body;

    public TCPMessage(String type, String key) {
        this.type = type;
        this.key = key;
        this.body = null;
    }

    public String getHeader(){
        return type + " " + key;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody(){
        return body;
    }

    public String getKey(){return key;}

    public String getDataStringStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((getHeader() + NL + NL ).getBytes());
        if(this.body != null) buffer.write(this.body.getBytes());

        return buffer.toString();
    }

    public String getType() {
        return type;
    }
}
