package routing;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import network.RequestHandler;
import network.Response;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestHandlerTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldTestRequestHandling() throws IOException {

        NodeInfo nodeInfo = new NodeInfo("node-0", 9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1", 9091);
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration("node-0", List.of(nodeInfo, nodeInfo1));
        NodeManager nodeManager = new NodeManager(clusterConfiguration, tempDir.resolve("node-0.data"));
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
        RequestHandler requestHandler = new RequestHandler(shardManager, clusterConfiguration, nodeManager);

        Response actualResponse = requestHandler.handleRequest("PUT|user:1|Sahil_1");
        Response expectedResponse = new Response(Response.Status.SUCCESS, "node-0", "operation successfull");

        assertEquals(actualResponse, expectedResponse);
    }
}
