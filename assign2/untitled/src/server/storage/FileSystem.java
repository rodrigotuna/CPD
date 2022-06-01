package server.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileSystem {

    private String path;
    private String hashId;

    public FileSystem(String accessPoint, String hashId){
        this.path = "filesystem/" + hashId + "/";
        this.hashId = hashId;
        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public void put(String key, String fileContent){
        try {
            File file = new File(path + key + ".file");
            if (!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();

            FileWriter valueWriter = new FileWriter(file);
            valueWriter.write(fileContent);

            valueWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key){
        try {
            File file = new File(path + key);
            if (!file.exists()) throw new FileNotFoundException();

            if(!file.delete()) throw new IOException();

            if(file.getParentFile().isDirectory() && Objects.requireNonNull(file.getParentFile().list()).length == 0)
                if(!file.getParentFile().delete()) throw new IOException();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> getFiles() {
        File dir = new File(path);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .collect(Collectors.toList());
    }
}
