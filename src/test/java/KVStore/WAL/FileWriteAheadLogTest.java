package KVStore.WAL;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileWriteAheadLogTest {

    @Test
    public void shouldTestDirCreationOrExistence() {
        FileWriteAheadLog fileWriteAheadLog = new FileWriteAheadLog();
        boolean isDirPresent=Files.exists(Path.of("data"));
        assertTrue(isDirPresent);
    }

    @Test
    public void shouldAppendToWal() {
        FileWriteAheadLog fileWriteAheadLog = new FileWriteAheadLog();
        WalRecord expectedWalRecord = new WalRecord(Operation.PUT, "user:1","Sahil");
        fileWriteAheadLog.append(expectedWalRecord);

        List<WalRecord> actualWalRecord = fileWriteAheadLog.readAll();
        assertEquals(1,actualWalRecord.size());
    }
}
