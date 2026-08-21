package routing;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import node.KVNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShardManagerTest {

    @Test
    public void shouldReturnSameShardForKey() throws IOException {
        KVNode node = new KVNode("node-0", new PersistedKVStore(new FileWriteAheadLog()));
        KVNode node1 = new KVNode("node-1", new PersistedKVStore(new FileWriteAheadLog()));
        ShardManager shardManager = new ShardManager(List.of(node, node1));
        assertEquals(node, shardManager.getNode("user:1"));
        assertEquals(node1, shardManager.getNode("user:2"));
        assertEquals(shardManager.getNode("user:3"), shardManager.getNode("user:3"));
    }
}
