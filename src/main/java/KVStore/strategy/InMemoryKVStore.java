package KVStore.strategy;

import KVStore.KVStore;
import KVStore.WAL.FileWriteAheadLog;
import KVStore.WAL.Operation;
import KVStore.WAL.WalRecord;
import KVStore.WAL.WriteAheadLog;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKVStore<K,V> implements KVStore<K,V> {

    private final ConcurrentHashMap<K,V> map;

    public InMemoryKVStore() {
        map = new ConcurrentHashMap<>();
    }
    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) throws IOException {
        map.put(key, value);
    }

    @Override
    public void delete(K key) {
        map.remove(key);
    }
}
