package routing;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShardManagerTest {

    @Test
    public void shouldReturnSameShardForKey() throws IOException {
        NodeInfo nodeInfo = new NodeInfo("node-0", 9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1", 9091);
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration("node-0", List.of(nodeInfo, nodeInfo1));
        NodeManager nodeManager = new NodeManager(clusterConfiguration, "node-0.data");
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
        assertEquals(nodeInfo, shardManager.getNode("user:1"));
        assertEquals(nodeInfo1, shardManager.getNode("user:2"));
        assertEquals(shardManager.getNode("user:3"), shardManager.getNode("user:3"));
    }
}
