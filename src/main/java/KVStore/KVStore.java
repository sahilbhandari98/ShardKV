package KVStore;

import java.io.IOException;

public interface KVStore<K,V> {
    V get(K key);
    void put(K key, V value) throws IOException;
    void delete(K key);
}
