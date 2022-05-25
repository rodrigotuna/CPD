package client;

import server.message.PutMessage;
import utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;


public class Client {

    private final Socket socket;
    public Client(String nodeAccessPoint) throws URISyntaxException, IOException {
        URI uri = new URI(nodeAccessPoint);
        socket = new Socket(uri.getHost(), uri.getPort());
    }

    public String put(String filePathname) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePathname);
        if(!file.exists()) throw new FileNotFoundException();

        OutputStream messageStream = this.socket.getOutputStream();

        byte[] fileContent = Files.readAllBytes(file.toPath());
        String fileKey = Utils.bytesToHexString(Utils.hash256(fileContent));

        messageStream.write((new PutMessage("PUT", fileKey, fileContent)).getDataByteStream());
        messageStream.flush();

        socket.shutdownOutput();
        return fileKey;
    }

    public void get(String hashcode) {

    }

    public void delete(String hashcode) {

    }
}
