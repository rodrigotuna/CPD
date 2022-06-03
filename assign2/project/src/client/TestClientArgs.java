package client;

public class TestClientArgs {
    private String nodeAccessPoint;
    private String operation;
    private String filePathname;
    private String hashcode;

    private boolean isMembership;

    public String getNodeAccessPoint() {
        return nodeAccessPoint;
    }

    public void setNodeAccessPoint(String nodeAccessPoint) {
        this.nodeAccessPoint = nodeAccessPoint;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFilePathname() {
        return filePathname;
    }

    public void setFilePathname(String filePathname) {
        this.filePathname = filePathname;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public boolean isMembership() {
        return isMembership;
    }

    public void setMembership(boolean membership) {
        isMembership = membership;
    }
}
