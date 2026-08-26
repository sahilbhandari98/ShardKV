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
    private ClusterConfiguration clusterConfiguration;

    public ShardManager(NodeManager nodeManager, ClusterConfiguration clusterConfiguration) {
        this.nodeManager = nodeManager;
        this.clusterConfiguration = clusterConfiguration;
        this.keyPartitioner = new KeyPartitioner(nodeManager.clusterSize());
    }
    public NodeInfo getNode(String key) throws IOException {
        int shard = keyPartitioner.getShard(key);
        String nodeId = "node-"+shard;
        System.out.println("node id is "+nodeId);
        return clusterConfiguration.getNode(nodeId);
    }

}
