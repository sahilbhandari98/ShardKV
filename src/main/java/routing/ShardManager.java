package routing;

import node.KVNode;

import java.util.List;

public class ShardManager {
    private KeyPartitioner keyPartitioner;
    private List<KVNode> kvNodes;

    public ShardManager(List<KVNode> kvNodes) {
        this.keyPartitioner = new KeyPartitioner(kvNodes.size());
        this.kvNodes = kvNodes;
    }
    public KVNode getNode(String key) {
        int shard = keyPartitioner.getShard(key);
        return kvNodes.get(shard);
    }
}
