package KVStore.WAL;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileWriteAheadLog implements WriteAheadLog{
    private Path dirPath;
    //private Path filePath;
    private static final Path DEFAULT_PATH = Path.of("data","wal.log");
    private final Path filePath;

    public FileWriteAheadLog() {
        this(DEFAULT_PATH);
    }
    public FileWriteAheadLog(Path filePath) {
        this.filePath = filePath;
        createWalIfNotPresent();
    }

    private void createWalIfNotPresent() {
        try {
            Path parent = filePath.getParent();
            if(parent != null) {
                Files.createDirectories(parent);
            }
            if(Files.notExists(filePath)) {
                try {
                    Files.createFile(filePath);
                } catch(FileAlreadyExistsException fileAlreadyExistsException) {
                    // file path already exists
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void append(WalRecord walRecord) throws IOException {
        String appendRecord = "";
        if(Operation.PUT.equals(walRecord.getOperation()))
            appendRecord = walRecord.getOperation() + "|" + walRecord.getKey() + "|" + walRecord.getValue()+"\n";
        else if(Operation.DELETE.equals(walRecord.getOperation()))
            appendRecord = walRecord.getOperation() + "|" + walRecord.getKey() + "\n";
        Files.write(filePath, appendRecord.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public List<WalRecord> readAll() throws IOException {
    List<WalRecord> walRecords = new ArrayList<>();
        List<String> records = Files.readAllLines(filePath);
        System.out.println(records);
        for(String record: records) {
            String[] values = record.split("\\|");
            if(Operation.PUT.equals(Operation.valueOf(values[0])))
                walRecords.add(new WalRecord(Operation.valueOf(values[0]), values[1], values[2]));
            else if(Operation.DELETE.equals(Operation.valueOf(values[0])))
                walRecords.add(new WalRecord(Operation.valueOf(values[0]), values[1]));
        }
        return walRecords;
    }
}
