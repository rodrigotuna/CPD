package server.storage;

import utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public File put(String key, String fileContent, int factor){
        try {
            File file = new File(path + factor + "/" + key);
            if (!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();

            FileWriter valueWriter = new FileWriter(file);
            valueWriter.write(fileContent);

            valueWriter.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key, int factor){
        try {
            File file = new File(path + factor + "/" + key);
            if (!file.exists()) throw new FileNotFoundException();
            File tombstone = new File(path + factor + "/tombstone" + key);
            file.renameTo(tombstone);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key, int factor) {
        try {
            File file = new File(path + factor + "/" + key);
            if (!file.exists()) throw new FileNotFoundException();

            byte[] fileContent = Files.readAllBytes(file.toPath());

            return Utils.bytesToString(fileContent);

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
