package KVStore;

import KVStore.strategy.KVStoreType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistedKVStoreTest {

    @Test
    public void testWalRecovery() throws IOException {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory(KVStoreType.PERSISTED);
        store.put("user:1","Sahil");
        assertEquals("Sahil",store.get("user:1"));
    }
}
