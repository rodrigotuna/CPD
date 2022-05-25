package server.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MembershipLog {
    private final String hashId;
    private final File file;

    private List<String> entryList = new ArrayList<>();
    private Map<String, Integer> mostRecent = new TreeMap<>();
    private int membershipCounter;

    public boolean addEntry(String nodeId, int membershipCounter) throws IOException {
        if(mostRecent.containsKey(nodeId) && mostRecent.get(nodeId) >= membershipCounter ){
            return false;
        }
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(nodeId + ";" + membershipCounter + "\n");
        fileWriter.close();
        mostRecent.put(nodeId, membershipCounter);
        return true;
    }

    public MembershipLog(String hashId) throws IOException {
        this.hashId = hashId;
        file = new File(hashId + ".log");
        if(!file.exists()){
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(-1 + "\n");
            membershipCounter = -1;
            fileWriter.close();
        }else{
            FileReader fileReader = new FileReader(file);
            membershipCounter = fileReader.read();
        }
    }

    public int getMembershipCounter() {
        return membershipCounter;
    }

    public void incrementCounter() {
        membershipCounter++;
        //change file line here i think; mas tipo uma só???
    }
}
