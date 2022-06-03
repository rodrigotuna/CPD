package server.storage;

import com.sun.source.tree.Tree;
import server.message.TCPMembershipMessage;
import utils.Utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MembershipLog {
    private final static int NUM_LOGS = 32;
    private final String hashId;
    private final File file;
    private final ConcurrentHashMap<String, Integer> mostRecent = new ConcurrentHashMap<>();
    private int membershipCounter;
    private String mostRecentlyUpdated;

    private final List<String> left;
    private final List<String> join;

    private int penalty;

    public void addEntry(String nodeId, int membershipCounter) throws IOException {
        if(mostRecent.containsKey(nodeId) && mostRecent.get(nodeId) >= membershipCounter ){
            return;
        }
        updateFileLine(nodeId,membershipCounter);
    }

    public MembershipLog(String hashId) throws IOException {
        this.hashId = hashId;
        this.penalty = 0;
        left = new ArrayList<>();
        join = new ArrayList<>();
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
                addToList(entries[0], Integer.parseInt(entries[1]));
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
        Scanner sc = null;
        ArrayList<String> content = new ArrayList<>();
        synchronized (file){
            sc = new Scanner(file);
            while (sc.hasNext()) {
                String entry = sc.next();
                content.add(entry);
            }
        }
        return content;
    }

    private void addToList(String accessPoint, int membershipCounter){
        if(membershipCounter % 2 == 0){
            join.add(accessPoint);
        } else{
            left.add(accessPoint);
        }
    }


    public ConfigurationFails mergeLog(String log) throws IOException {
        ConfigurationFails cf = new ConfigurationFails();
        ArrayList<String> logContent =  getContents();

        String [] entryList = log.split("\n");

        boolean changes = false;

        for(String entry : entryList) {
            String[] entryValues = entry.split(";");
            if (!mostRecent.containsKey(entryValues[0])) {
                changes = true;
                logContent.add(entry);
                mostRecent.put(entryValues[0], Integer.parseInt(entryValues[1]));
                cf.add(entryValues[0], Integer.parseInt(entryValues[1]));
                penalty++;
            } else {
                if (mostRecent.get(entryValues[0]) < Integer.parseInt(entryValues[1])) {
                    changes = true;
                    logContent.remove(entryValues[0] + ";" + mostRecent.get(entryValues[0]));
                    logContent.add(entry);
                    mostRecent.put(entryValues[0], Integer.parseInt(entryValues[1]));
                    cf.add(entryValues[0], Integer.parseInt(entryValues[1]));
                    penalty++;
                }
            }
        }

        if(!changes) return cf;
        synchronized (file){
            FileWriter fw = new FileWriter(file);
            for(String s : logContent){
                fw.write(s + "\n");
            }
            fw.close();
        }

        return cf;
    }

    public List<String> nodesJoined(){
        List<String> ret = new ArrayList<>();
        for(String s : mostRecent.keySet()){
            if(mostRecent.get(s) % 2 ==0){
                ret.add(s);
            }
        }
        return ret;
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

    public int getPenalty() {
        return penalty;
    }
}
