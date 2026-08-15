package KVStore;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.KVStoreType;
import KVStore.strategy.PersistedKVStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistedKVStoreTest {

    @TempDir
    Path tempDir;

    @Test
    public void testWalRecovery() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        store.put("user:1","Sahil");
        assertEquals("Sahil",store.get("user:1"));
    }
}
