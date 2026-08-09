package KVStore.strategy;

import KVStore.KVStore;

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
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void delete(K key) {
        map.remove(key);
    }
}
