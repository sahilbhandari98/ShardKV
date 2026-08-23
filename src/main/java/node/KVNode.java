package node;

import KVStore.KVStore;

import java.io.IOException;

public class KVNode {
    private String nodeID;
    private int port;
    private KVStore<String, String> kvStore;

    public KVNode(String nodeID, int port, KVStore kvStore) {
        this.nodeID = nodeID;
        this.port = port;
        this.kvStore = kvStore;
    }

    public void put(String key, String value) throws IOException {
        kvStore.put(key, value);
    }

    public String get(String key) {
        return kvStore.get(key);
    }

    public void delete(String key) throws IOException {
        kvStore.delete(key);
    }

    public String getNodeID() {
        return this.nodeID;
    }
}
