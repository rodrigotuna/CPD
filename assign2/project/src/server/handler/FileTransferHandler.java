package server.handler;

import client.Client;
import server.Node;
import server.message.PutMessage;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class FileTransferHandler implements Runnable{
    private final File file;
    private final URI uri;

    private final Node node;

    private final int factor;

    private final boolean delete;


    public FileTransferHandler(File file, String accessPoint, Node node, int factor, boolean delete) {
        this.file = file;
        this.node = node;
        this.factor = factor;
        this.delete = delete;
        try {
            this.uri = new URI(null, accessPoint, null, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        boolean connecting = true;
        Socket socket = null;
        while(connecting){
            try {
                socket = new Socket(uri.getHost(), uri.getPort());
                connecting = false;
            } catch (IOException e) {
                connecting = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        try {
            OutputStream messageStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(messageStream, true);

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String fileKey = Utils.bytesToHexString(Utils.hash256(fileContent));
            writer.println((new PutMessage( factor , fileKey).getDataStringStream()));


            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String code = bufferedReader.readLine();
            switch(Integer.parseInt(code)){
                case 200:
                    messageStream.write(fileContent);
                    messageStream.flush(); messageStream.close();
                    if(delete) file.delete();
                    break;
                case 300:
                    String accessPoint = bufferedReader.readLine();
                    node.executeThread(new FileTransferHandler(file, accessPoint, node, factor, delete));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
