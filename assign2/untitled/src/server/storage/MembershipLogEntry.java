package server.storage;

public class MembershipLogEntry {
    private String nodeId;
    private String membershipCounter;

    public MembershipLogEntry(String nodeId, String membershipCounter) {
        this.nodeId = nodeId;
        this.membershipCounter = membershipCounter;
    }


    public String getNodeid() {
        return nodeId;
    }

    public String getMembershipCounter() {
        return membershipCounter;
    }

    public String toString(){
        return nodeId + ";" + membershipCounter + "\n";
    }
}
