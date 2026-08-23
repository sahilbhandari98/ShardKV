package routing;

import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import node.KVNode;
import node.NodeInfo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ShardManager {
    private KeyPartitioner keyPartitioner;
    ClusterConfiguration clusterConfiguration;

    public ShardManager(ClusterConfiguration clusterConfiguration) {
        this.keyPartitioner = new KeyPartitioner(clusterConfiguration.getAllNodes().size());
        this.clusterConfiguration = clusterConfiguration;
    }
    public KVNode getNode(String key) throws IOException {
        int shard = keyPartitioner.getShard(key);
        String nodeId = "node-"+shard;
        System.out.println("node id is "+nodeId);
        System.out.println("Current cluster node is : "+clusterConfiguration.getCurrentNodeId());
        if(nodeId.equalsIgnoreCase(clusterConfiguration.getCurrentNodeId())) {
            Optional<NodeInfo> nodeInfo =
                    clusterConfiguration.
                            getAllNodes()
                            .stream()
                            .filter(node -> node.getNodeId().equals(nodeId)).findAny();
            return new KVNode(clusterConfiguration.getCurrentNodeId(), nodeInfo.get().getPort(),new PersistedKVStore<>());
        } else {
            // TODO: need to route to the correct jvm
            System.out.println("no matching jvm");
            return null;
        }
    }
}
