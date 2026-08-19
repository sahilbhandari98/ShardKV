package KVStore.WAL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileWriteAheadLogTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldAppendToWal() throws IOException {
        Path tempFilePath = tempDir.resolve("wal.log");
        FileWriteAheadLog fileWriteAheadLog = new FileWriteAheadLog(tempFilePath);
        WalRecord expectedWalRecord1 = new WalRecord(Operation.PUT, "user:1","Sahil");
        WalRecord expectedWalRecord2 = new WalRecord(Operation.PUT, "user:2","Rahul");
        fileWriteAheadLog.append(expectedWalRecord1);
        fileWriteAheadLog.append(expectedWalRecord2);

        List<WalRecord> actualWalRecord = fileWriteAheadLog.readAll();
        actualWalRecord.forEach(x -> System.out.println());
        //assertTrue(actualWalRecord.contains(expectedWalRecord1));
        assertEquals(2, actualWalRecord.size());
        assertEquals(expectedWalRecord1, actualWalRecord.get(0));
        assertEquals(expectedWalRecord2, actualWalRecord.get(1));
    }
}
