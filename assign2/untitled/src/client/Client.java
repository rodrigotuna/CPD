package client;

import server.message.DeleteMessage;
import server.message.GetMessage;
import server.message.PutMessage;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Client {

    private final Socket socket;
    private final String nodeAccessPoint;
    public Client(String nodeAccessPoint) throws URISyntaxException, IOException {
        this.nodeAccessPoint = nodeAccessPoint;
        URI uri = new URI(null, nodeAccessPoint, null, null, null);
        socket = new Socket(uri.getHost(), uri.getPort());
    }

    public String put(String filePathname) {
        try {
            File file = new File(filePathname);
            if (!file.exists()) throw new FileNotFoundException();

            OutputStream messageStream = this.socket.getOutputStream();
            PrintWriter writer = new PrintWriter(messageStream, true);

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String fileKey = Utils.bytesToHexString(Utils.hash256(fileContent));
            writer.println((new PutMessage(fileKey).getDataStringStream()));


            InputStream inputStream = this.socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String code = bufferedReader.readLine();
            System.out.println(code);
            switch(Integer.parseInt(code)){
                case 200:
                    System.out.println("200: OK Successful Operation");
                    messageStream.write(fileContent);
                    break;
                case 300:
                    System.out.println("300: Redirect");
                    String accessPoint = bufferedReader.readLine();
                    return new Client(accessPoint).put(filePathname);
            }


            return fileKey;
        }
        catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public void get(String hashcode){
        try {
            OutputStream messageStream = this.socket.getOutputStream();

            PrintWriter messageWriter = new PrintWriter(messageStream, true);
            messageWriter.println((new GetMessage(hashcode)).getDataStringStream());

            InputStream inputStream = this.socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String code = bufferedReader.readLine();
            System.out.println(code);
            switch(Integer.parseInt(code)){
                case 200:

                    byte[] fileContent = inputStream.readAllBytes();

                    File file = new File(hashcode);
                    FileWriter valueWriter = new FileWriter(file);
                    file.createNewFile();

                    valueWriter.write(Utils.bytesToString(fileContent));
                    valueWriter.close();

                    System.out.println("200: OK Successful Operation");
                    break;
                case 300:
                    System.out.println("300: Redirect");
                    String accessPoint = bufferedReader.readLine();
                    new Client(accessPoint).get(hashcode);
            }
        }
        catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(String hashcode) {
        try{
            OutputStream messageStream = this.socket.getOutputStream();

            PrintWriter messageWriter = new PrintWriter(messageStream, true);
            messageWriter.println((new DeleteMessage(hashcode)).getDataStringStream());

            InputStream inputStream = this.socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String code = bufferedReader.readLine();
            System.out.println(code);
            switch(Integer.parseInt(code)){
                case 200:
                    System.out.println("200: OK Successful Operation");
                    break;
                case 300:
                    System.out.println("300: Redirect");
                    String accessPoint = bufferedReader.readLine();
                    new Client(accessPoint).delete(hashcode);
            }
        }
        catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
}
