import server.Node;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Store {
    public static void main(String[] args){
        if(args.length != 4){
            System.out.println(usage());
            return;
        }

        try {
            Node node = new Node(new InetSocketAddress(InetAddress.getByName(args[0]), Integer.parseInt(args[1])),
                                 new InetSocketAddress(InetAddress.getByName(args[2]), Integer.parseInt(args[3])));
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private static String usage(){
        return "Usage: \njava Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port>";
    }
}
