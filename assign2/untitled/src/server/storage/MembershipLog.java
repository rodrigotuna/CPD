package server.storage;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class MembershipLog {
    private final String hashId;
    private final File file;
    private Map<String, Integer> mostRecent = new TreeMap<>();
    private int membershipCounter;

    public boolean addEntry(String nodeId, int membershipCounter) throws IOException {
        if(mostRecent.containsKey(nodeId) && mostRecent.get(nodeId) >= membershipCounter ){
            return false;
        }
        updateFileLine(nodeId,membershipCounter);
        return true;
    }

    public MembershipLog(String hashId) throws IOException {
        this.hashId = hashId;
        file = new File(hashId + ".log");
        if(!file.exists()){
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(hashId + ";" + -1 + "\n");
            membershipCounter = -1;
            fileWriter.close();
        }else{
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String[] entries = sc.next().split(";");
                mostRecent.put(entries[0], Integer.parseInt(entries[1]));
            }
        }
    }

    public int getMembershipCounter() {
        return membershipCounter;
    }

    public void incrementCounter() throws IOException {
        membershipCounter++;
        updateFileLine(hashId, membershipCounter);
    }

    public void updateFileLine(String nodeId, int membershipCounter) throws IOException {
        mostRecent.put(nodeId, membershipCounter);
        Scanner sc = new Scanner(file);
        StringBuilder content = new StringBuilder();
        while (sc.hasNext()) {
            String entry = sc.next();
            if(!entry.contains(nodeId)) {
                content.append(entry).append(System.getProperty("line.separator"));
            }
        }
        content.append(nodeId).append(";").append(membershipCounter).append(System.getProperty("line.separator"));
        FileWriter fw = new FileWriter(file);
        fw.write(content.toString());
        fw.close();
    }
}
