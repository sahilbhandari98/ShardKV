package routing;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShardManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldReturnSameShardForKey() throws IOException {
        NodeInfo nodeInfo = new NodeInfo("node-0", 9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1", 9091);
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration("node-0", List.of(nodeInfo, nodeInfo1));
        NodeManager nodeManager = new NodeManager(clusterConfiguration, tempDir.resolve("node-0.data"));
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
        assertEquals(nodeInfo, shardManager.getShardPlacement("user:1").getPrimary());
        assertEquals(nodeInfo1, shardManager.getShardPlacement("user:2").getPrimary());
        assertEquals(shardManager.getShardPlacement("user:3").getPrimary(), shardManager.getShardPlacement("user:3").getPrimary());
    }
}
