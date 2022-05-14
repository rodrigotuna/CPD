import client.Client;
import client.TestClientArgs;
import server.MembershipInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClient {
    private final static TestClientArgs functionArgs = new TestClientArgs();
    public static void main(String [] args){
        if(!parseArgs(args)){
            System.out.println(usage());
            return;
        }

        try {

            if(functionArgs.isMembership()){
                Registry registry = LocateRegistry.getRegistry();
                MembershipInterface node =  (MembershipInterface) registry.lookup(functionArgs.getNodeAccessPoint());

                switch(functionArgs.getOperation()) {
                    case "join":
                        node.join();
                    case "leave":
                        node.leave();
                }
            } else {
                Client client = new Client(functionArgs.getNodeAccessPoint());
                switch(functionArgs.getOperation()){
                    case "put":
                        client.put(functionArgs.getFilePathname());
                    case "get":
                        client.get(functionArgs.getHashcode());
                    case "delete":
                        client.delete(functionArgs.getHashcode());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean parseArgs(String[] args){
        if(args.length < 2 || args.length > 3){
            return false;
        }

        functionArgs.setNodeAccessPoint(args[0]);
        functionArgs.setOperation(args[1]);

        boolean isMembership = false;

        switch(args[1]){
            case "join":
            case "leave":
                functionArgs.setMembership(true);
                break;

            case "put":
                functionArgs.setFilePathname(args[2]);
                functionArgs.setMembership(false);
                break;

            case "get":
            case "delete":
                functionArgs.setHashcode(args[2]);
                functionArgs.setMembership(false);
                break;

            default: return false;
        }

        return true;
    }

    private static String usage(){
        return "Usage:\njava TestClient <node_ap> <operation> [<opnd>]\n";
    }
}
