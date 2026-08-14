package KVStore.strategy;

import KVStore.KVStore;
import KVStore.WAL.FileWriteAheadLog;
import KVStore.WAL.Operation;
import KVStore.WAL.WalRecord;
import KVStore.WAL.WriteAheadLog;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PersistedKVStore<K,V> implements KVStore<K,V> {

    private final ConcurrentHashMap<K,V> map;
    private final WriteAheadLog writeAheadLog;

    public PersistedKVStore() throws IOException {
        writeAheadLog = new FileWriteAheadLog();
        map = new ConcurrentHashMap<>();
        recovery();
    }
    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) throws IOException {
        writeAheadLog.append(new WalRecord(Operation.PUT, (String) key, (String) value));
        map.put(key, value);
    }

    @Override
    public void delete(K key) throws IOException {
        writeAheadLog.append(new WalRecord(Operation.DELETE, (String) key));
        map.remove(key);
    }

    @SuppressWarnings("unchecked")
    public void recovery() throws IOException {
        List<WalRecord> walRecords = writeAheadLog.readAll();
        for(WalRecord walRecord : walRecords) {
            if(Operation.PUT.equals(walRecord.getOperation())) {
                map.put((K) walRecord.getKey(), (V)walRecord.getValue());
            } else if(Operation.DELETE.equals(walRecord.getOperation())) {
                map.remove(walRecord.getKey());
            }
        }
    }
}
