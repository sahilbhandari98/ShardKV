package KVStore.WAL;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        Files.write(filePath,
                appendRecord.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND,
                StandardOpenOption.SYNC);
    }

    @Override
    public List<WalRecord> readAll() throws IOException {
    List<WalRecord> walRecords = new ArrayList<>();
        String records = Files.readString(filePath);
        if(records.isBlank() || records.isEmpty()) {
            System.out.println("Empty WAL file");
            return walRecords;
        }
        int lastNewLine = records.lastIndexOf('\n');
        boolean endsWithNewLine = records.endsWith("\n");

        if(!endsWithNewLine) {
            try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE)) {
                channel.truncate(lastNewLine + 1);
            }
        }
        //System.out.println("Raw text from file "+records);
        String[] lines = records.split("\n");
        //System.out.println(Arrays.toString(lines));
        for(int i=0;i<lines.length;i++) {
            if(i == lines.length - 1 && !endsWithNewLine) {
                System.out.println("Ignoring partially written record");
                continue;
            }
            String record = lines[i];
            String[] values = record.split("\\|");
            try {
                Operation operation = Operation.valueOf(values[0]);
                if (values.length == 3 && Operation.PUT.equals(operation))
                    walRecords.add(new WalRecord(Operation.valueOf(values[0]), values[1], values[2]));
                else if (values.length == 2 && Operation.DELETE.equals(operation))
                    walRecords.add(new WalRecord(Operation.valueOf(values[0]), values[1]));
                else
                    System.out.println("Ignore Invalid WAL Record");
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Ignore invalid WAL record "+illegalArgumentException.getMessage());
            }
        }
        return walRecords;
    }
}
