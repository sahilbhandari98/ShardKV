package routing;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.RequestHandler;
import network.Response;
import node.KVNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestHandlerTest {

    @TempDir
    Path tempPath;

    @Test
    public void shouldTestRequestHandling() throws IOException {
//        KVNode node = new KVNode("node-0", new PersistedKVStore
//                (new FileWriteAheadLog(tempPath.resolve(Path.of("data","wal0.log")))));
//        KVNode node1 = new KVNode("node-1", new PersistedKVStore
//                (new FileWriteAheadLog(tempPath.resolve(Path.of("data","wal1.log")))));
//        ShardManager shardManager = new ShardManager(List.of(node, node1));
//        RequestHandler requestHandler = new RequestHandler(shardManager);
//
//        Response actualResponse = requestHandler.handleRequest("PUT|user:1|Sahil");
//        Response expectedResponse = new Response(Response.Status.SUCCESS, "node-0", "operation successfull");
//
//        assertEquals(actualResponse, expectedResponse);
    }
}
