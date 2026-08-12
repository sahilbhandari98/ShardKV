package KVStore;

import KVStore.strategy.KVStoreType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistedKVStoreTest {

    @Test
    public void testWalRecovery() {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory(KVStoreType.PERSISTED);
        assertEquals("Sahil",store.get("user:1"));
    }
}
