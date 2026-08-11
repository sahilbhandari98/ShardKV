package KVStore.strategy;

import KVStore.KVStore;
import KVStore.WAL.FileWriteAheadLog;
import KVStore.WAL.Operation;
import KVStore.WAL.WalRecord;
import KVStore.WAL.WriteAheadLog;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKVStore<K,V> implements KVStore<K,V> {

    private final ConcurrentHashMap<K,V> map;
    private final WriteAheadLog writeAheadLog;

    public InMemoryKVStore() {
        writeAheadLog = new FileWriteAheadLog();
        map = new ConcurrentHashMap<>();
    }
    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        writeAheadLog.append(new WalRecord(Operation.PUT,(String) key, (String)value));
        map.put(key, value);
    }

    @Override
    public void delete(K key) {
        map.remove(key);
    }
}
