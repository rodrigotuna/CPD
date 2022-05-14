package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    private final Socket socket;
    public Client(String nodeAccessPoint) throws URISyntaxException, IOException {
        URI uri = new URI(nodeAccessPoint);
        socket = new Socket(uri.getHost(), uri.getPort());
    }

    public String put(String filePathname) {
        File file = new File(filePathname);
        return "";
    }

    public void get(String hashcode) {

    }

    public void delete(String hashcode) {

    }
}
