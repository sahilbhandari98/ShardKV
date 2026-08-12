package KVStore;

import KVStore.strategy.InMemoryKVStore;
import KVStore.strategy.PersistedKVStore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class KvStoreFactory {
    static ConcurrentHashMap<String, Supplier<KVStore<?,?>>> strategies = new ConcurrentHashMap<>();;

    static {
        strategies.put("IN_MEMORY", () -> new InMemoryKVStore<>());
    }

    public static <K,V> KVStore<K,V> getKvStoreFactory(String type) {
        Supplier<KVStore<?,?>> supplier = strategies.getOrDefault(type, InMemoryKVStore::new);
        @SuppressWarnings("unchecked")
        KVStore<K,V> kvstore = (KVStore<K,V>) supplier.get();
        return kvstore;
    }

     public static <K,V> KVStore<K,V> getKvStoreFactory() {
         return new PersistedKVStore<>();
     }

}
