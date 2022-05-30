package server.storage;

import utils.Utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Ring {
    private TreeMap<String, String> ring;

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

    public String getResponsible(String fileId){
        return ring.get(Utils.circularUpperBound((TreeSet) ring.keySet(), fileId));
    }

    public void mergeRing(String ring) {
        String [] entryList = ring.split("\n");
        for(String entry : entryList) {
            String[] entryValues = entry.split(";");
            addMember(entryValues[0], entryValues[1]);
        }
    }
}
