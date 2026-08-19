package KVStore;

import KVStore.WAL.FailingWriteAheadLog;
import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PersistedKVStoreTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldCreateWalFile() {
        Path walFilePath = tempDir.resolve("data.log");
        new FileWriteAheadLog(walFilePath);

        assertTrue(Files.exists(walFilePath));
        assertTrue(Files.isRegularFile(walFilePath));
    }

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

    @Test
    public void shouldRecoverFromPartiallyWrittenRecords() throws IOException {
        Path walPath = tempDir.resolve("wal.log");
        Files.writeString(walPath,
                "PUT|user:1|Sahil\n"+
                "PUT|user:2|Rahul\n"+
                "PUT|user:3|Sah"
        );

        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));

        assertEquals("Sahil", store.get("user:1"));
        assertEquals("Rahul", store.get("user:2"));
        assertNull( store.get("user:3"));

        store.put("user:1","Harshit");
        store.put("user:4","Samar");
        store.delete("user:4");

        assertEquals("Harshit", store.get("user:1"));
        assertNull(store.get("user:4"));
    }

    @Test
    public void shouldRecoverFromMalformedRecords() throws IOException {
        Path walPath = tempDir.resolve("wal.log");
        Files.writeString(walPath,
                "PUT|user:4|"+
                        "PUT|user:1|Sahil|Rahul"+
                        "INVALID|user:2|Rahul\n"+
                        "DELETE"
        );

        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));

        assertNull( store.get("user:1"));
    }

    @Test
    public void shouldReturnEmptyValueCorrectly() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        store.put("user:1", "");

        store = new PersistedKVStore<>(new FileWriteAheadLog(tempDir.resolve("wal.log")));
        assertEquals("", store.get("user:1"));
    }

    @Test
    public void shouldNotUpdateMapWhenWriteFails() throws IOException {
        KVStore<String, String> store = new PersistedKVStore<>(new FailingWriteAheadLog());

        assertThrows(IOException.class, () -> store.put("user:1","sahil"));
        assertNull(store.get("user:1"));

    }
}
