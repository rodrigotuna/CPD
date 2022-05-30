package server.storage;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class MembershipLog {
    private final static int NUM_LOGS = 32;
    private final String hashId;
    private final File file;
    private Map<String, Integer> mostRecent = new TreeMap<>();
    private int membershipCounter;
    private String mostRecentlyUpdated;

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
        mostRecentlyUpdated = hashId;
    }

    public int getMembershipCounter() {
        return membershipCounter;
    }

    public void incrementCounter() throws IOException {
        membershipCounter++;
        updateFileLine(hashId, membershipCounter);
    }

    private void updateFileLine(String nodeId, int membershipCounter) throws IOException {
        mostRecent.put(nodeId, membershipCounter);
        StringBuilder content = getContentsExcept(nodeId);
        content.append(nodeId).append(";").append(membershipCounter).append("\n");
        FileWriter fw = new FileWriter(file);
        fw.write(content.toString());
        fw.close();
    }

    private StringBuilder getContentsExcept(String nodeId) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        StringBuilder content = new StringBuilder();
        while (sc.hasNext()) {
            String entry = sc.next();
            if(!entry.contains(nodeId)) {
                content.append(entry).append("\n");
            }
        }
        return content;
    }

    private ArrayList<String> getContents() throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<String> content = new ArrayList<>();
        while (sc.hasNext()) {
            String entry = sc.next();
            content.add(entry);
        }
        return content;
    }

    public void mergeLog(String log) throws IOException {
        ArrayList<String> logContent =  getContents();
        String [] entryList = log.split("\n");
        for(String entry : entryList) {
            String[] entryValues = entry.split(";");
            if (!mostRecent.containsKey(entryValues[0])) {
                logContent.add(entry);
                mostRecent.put(entryValues[0], Integer.parseInt(entryValues[1]));
            } else if (mostRecent.get(entryValues[0]) < Integer.parseInt(entryValues[1])) {
                logContent.remove(entryValues[0] + ";" + mostRecent.get(entryValues[0]));
            }
        }
        FileWriter fw = new FileWriter(file);
        for(String s : logContent){
            fw.write(s + "\n");
        }
        fw.close();
    }

    public String mostRecentLogContent() throws FileNotFoundException {
        ArrayList<String> listContent = getContents();
        String [] content = new String[listContent.size()];
        content = listContent.toArray(content);

        return String.join("\n", Arrays.copyOfRange(content, Math.max(0, content.length - NUM_LOGS), content.length));
    }

    public String getMostRecentlyUpdated() {
        return mostRecentlyUpdated;
    }

    public void setMostRecentlyUpdated(String mostRecentlyUpdated) {
        this.mostRecentlyUpdated = mostRecentlyUpdated;
    }
}
