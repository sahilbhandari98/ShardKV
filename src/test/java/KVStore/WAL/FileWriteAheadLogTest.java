package KVStore.WAL;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    public void shouldAppendToWal() throws IOException {
        FileWriteAheadLog fileWriteAheadLog = new FileWriteAheadLog();
        WalRecord expectedWalRecord1 = new WalRecord(Operation.PUT, "user:1","Sahil");
        WalRecord expectedWalRecord2 = new WalRecord(Operation.PUT, "user:2","Rahul");
        fileWriteAheadLog.append(expectedWalRecord1);
        fileWriteAheadLog.append(expectedWalRecord2);

        List<WalRecord> actualWalRecord = fileWriteAheadLog.readAll();
        actualWalRecord.forEach(x -> System.out.println());
        assertTrue(actualWalRecord.contains(expectedWalRecord1));
    }
}
