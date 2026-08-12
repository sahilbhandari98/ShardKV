package KVStore.WAL;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileWriteAheadLog implements WriteAheadLog{
    private Path dirPath = Path.of("data");
    private Path filePath = dirPath.resolve("wal.log");
    public FileWriteAheadLog() {
        createWalIfNotPresent();
    }

    private void createWalIfNotPresent() {
        try {
            Files.createDirectories(dirPath);
            if(Files.notExists(filePath)) {
                try {
                    Files.createFile(filePath);
                } catch(FileAlreadyExistsException fileAlreadyExistsException) {

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void append(WalRecord walRecord) throws IOException {
        String appendRecord = walRecord.getOperation() + "|" + walRecord.getKey() + "|" + walRecord.getValue()+"\n";
        Files.write(filePath, appendRecord.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public List<WalRecord> readAll() {
        List<WalRecord> walRecords = new ArrayList<>();
        try {
            List<String> records = Files.readAllLines(filePath);
            System.out.println(records);
            for(String record: records) {
                String[] values = record.split("\\|");
                walRecords.add(new WalRecord(Operation.valueOf(values[0]), values[1], values[2]));
            }
            return walRecords;
        } catch(IOException exception) {
            exception.printStackTrace();
        } finally {
            return walRecords;
        }
    }
}
