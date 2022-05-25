package client;

import server.message.GetMessage;
import server.message.PutMessage;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Client {

    private final Socket socket;
    private final String nodeAccessPoint;
    public Client(String nodeAccessPoint) throws URISyntaxException, IOException {
        this.nodeAccessPoint = nodeAccessPoint;
        URI uri = new URI(nodeAccessPoint);
        socket = new Socket(uri.getHost(), uri.getPort());
    }

    public String put(String filePathname) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePathname);
        if(!file.exists()) throw new FileNotFoundException();

        OutputStream messageStream = this.socket.getOutputStream();
        PrintWriter writer = new PrintWriter(messageStream, true);

        byte[] fileContent = Files.readAllBytes(file.toPath());
        String fileKey = Utils.bytesToHexString(Utils.hash256(fileContent));

        writer.println((new PutMessage("PUT", fileKey, Arrays.toString(fileContent))).getDataByteStream());

        return fileKey;
    }

    public void get(String hashcode) throws IOException {
        OutputStream messageStream = this.socket.getOutputStream();
        InputStream valueStream = this.socket.getInputStream();

        PrintWriter messageWriter = new PrintWriter(messageStream, true);
        messageWriter.println((new GetMessage("GET", hashcode)).getDataByteStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(valueStream));
        StringBuilder fileContent = new StringBuilder();
        while(this.socket.isConnected())
            fileContent.append(reader.readLine());

        File file = new File("../FileSystem" +  this.nodeAccessPoint + "/" + hashcode + ".file");
        if(!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();

        FileWriter valueWriter = new FileWriter(file);
        if(file.createNewFile()) valueWriter.write(fileContent.toString());
    }

    public void delete(String hashcode) throws IOException {
        OutputStream messageStream = this.socket.getOutputStream();

        PrintWriter messageWriter = new PrintWriter(messageStream, true);
        messageWriter.println((new GetMessage("DELETE", hashcode)).getDataByteStream());

    }
}
