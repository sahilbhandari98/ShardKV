package KVStore;

import KVStore.strategy.InMemoryKVStore;
import KVStore.strategy.KVStoreType;
import KVStore.strategy.PersistedKVStore;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class KvStoreFactory {
    static ConcurrentHashMap<KVStoreType, Supplier<KVStore<?,?>>> strategies = new ConcurrentHashMap<>();;

    static {
        strategies.put(KVStoreType.IN_MEMORY, () -> new InMemoryKVStore<>());
        strategies.put(KVStoreType.PERSISTED, () -> {
            try {
                return new PersistedKVStore<>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <K,V> KVStore<K,V> getKvStoreFactory(KVStoreType type) {
        Supplier<KVStore<?,?>> supplier = strategies.getOrDefault(type, InMemoryKVStore::new);
        @SuppressWarnings("unchecked")
        KVStore<K,V> kvstore = (KVStore<K,V>) supplier.get();
        return kvstore;
    }

     public static <K,V> KVStore<K,V> getKvStoreFactory() {
         return new InMemoryKVStore<>();
     }

}
