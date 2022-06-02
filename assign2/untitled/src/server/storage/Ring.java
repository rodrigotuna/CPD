package server.storage;

import utils.Utils;

import java.util.*;

public class Ring {
    private TreeMap<String, String> ring;

    public static final int REPLICATION_FACTOR = 3;

    public Ring(){
        ring = new TreeMap<>();
    }

    public void addMember(String id, String accessPoint){
        ring.put(id, accessPoint);
    }

    public void removeMember(String id){
        ring.remove(id);
    }

    public String listMembers(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : ring.entrySet()){
            sb.append(entry.getKey()).append(";").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public List<String> getResponsible(String fileId){
        int factor = Math.min(ring.size(), REPLICATION_FACTOR);
        List<String> storeNodes = new ArrayList<>();

        String responsible = ring.get(Utils.circularUpperBound(ring, fileId));
        for(int i = 0; i < factor; i++) {
            storeNodes.add(responsible);
            responsible = ring.get(Utils.circularNextValue(ring,
                    Utils.bytesToHexString(Utils.hash256(responsible.getBytes()))));
        }
        System.out.println(storeNodes);
        return storeNodes;
    }

    public void mergeRing(String ring) {
        String [] entryList = ring.split("\n");
        for(String entry : entryList) {
            String[] entryValues = entry.split(";");
            addMember(entryValues[0], entryValues[1]);
        }
    }
}