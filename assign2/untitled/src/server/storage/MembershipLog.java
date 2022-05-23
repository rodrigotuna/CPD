package server.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MembershipLog {
    private final String hashId;
    private final File file;

    private List<MembershipLogEntry> entryList = new ArrayList<>();

    public void addEntry(MembershipLogEntry entry) throws IOException {
        entryList.add(entry);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(entry.toString());
        fileWriter.close();
    }

    public MembershipLog(String hashId){
        this.hashId = hashId;
        file = new File(hashId + ".log");
    }


}
