package server.handler;

import server.Node;
import server.message.DeleteMessage;
import server.message.PutMessage;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class DeleteFileOtherNode implements Runnable{
    private final String file;
    private final URI uri;

    private final Node node;

    private final int factor;


    public DeleteFileOtherNode(String file, String accessPoint, Node node, int factor) {
        this.file = file;
        this.node = node;
        this.factor = factor;

        try {
            this.uri = new URI(null, accessPoint, null, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(uri.getHost(), uri.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            OutputStream messageStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(messageStream, true);

            writer.println((new DeleteMessage(factor , file).getDataStringStream()));


            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String code = bufferedReader.readLine();
            switch(Integer.parseInt(code)){
                case 200:
                    break;
                case 300:
                    String accessPoint = bufferedReader.readLine();
                    node.executeThread(new DeleteFileOtherNode(file, accessPoint, node, factor));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
