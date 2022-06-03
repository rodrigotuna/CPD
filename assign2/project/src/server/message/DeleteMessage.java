package server.message;

public class DeleteMessage extends TCPMessage{
    private final int factor;
    public DeleteMessage(int factor, String key) {
        super("DELETE", key);
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
