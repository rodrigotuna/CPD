package server.storage;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationFails {
    private List<String> left;
    private List<String> join;

    ConfigurationFails(){
        left = new ArrayList<>();
        join = new ArrayList<>();
    }

    public List<String> getJoin() {
        return join;
    }

    public List<String> getLeft() {
        return left;
    }

    public void add(String node, int membershipCounter){
        if(membershipCounter % 2 == 0){
            join.add(node);
        }else{
            left.add(node);
        }
    }


}
