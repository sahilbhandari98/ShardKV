package KVStore;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersistedKVStoreTest {

    @TempDir
    Path tempDir;

    @Test
    public void testWalRecovery() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        store.put("user:1","Sahil");
        store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        assertEquals("Sahil",store.get("user:1"));
    }

    @Test
    public void shouldPreserveRecordsOrder() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        store.put("user:1","Sahil");
        store.put("user:2","Rahul");
        store.put("user:3","Ajay");
        store.put("user:1","Harshit");

        store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        assertEquals("Harshit",store.get("user:1"));
    }

    @Test
    public void shouldTestDeleteRecovery() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        store.put("user:1","Sahil");
        store.delete("user:1");

        store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        assertNull(store.get("user:1"));
    }
}
