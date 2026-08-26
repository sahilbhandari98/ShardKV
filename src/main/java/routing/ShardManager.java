package routing;

import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ShardManager {
    private KeyPartitioner keyPartitioner;
    private NodeManager nodeManager;

    public ShardManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
        this.keyPartitioner = new KeyPartitioner(nodeManager.clusterSize());
    }
    public KVNode getNode(String key) throws IOException {
        int shard = keyPartitioner.getShard(key);
        String nodeId = "node-"+shard;
        System.out.println("node id is "+nodeId);
        return nodeManager.getKvNode();
    }
}
