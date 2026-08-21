package routing;

import node.KVNode;

import java.util.List;

public class ShardManager {
    KeyPartitioner keyPartitioner;
    List<KVNode> kvNodes;

    public ShardManager(int numberOfShards, List<KVNode> kvNodes) {
        this.keyPartitioner = new KeyPartitioner(numberOfShards);
        this.kvNodes = kvNodes;
    }
    public KVNode getShard(String key) {
        int shard = keyPartitioner.getShard(key);
        return kvNodes.get(shard);
    }
}
