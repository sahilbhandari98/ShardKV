package network;

import node.NodeInfo;
import node.NodeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import routing.ShardManager;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestHandlerTest {

    @TempDir
    Path tempDir;
    RequestHandler requestHandler;

    @BeforeEach
    public void setup() throws IOException {
        NodeInfo nodeInfo = new NodeInfo("node-0", 9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1", 9091);
        NodeInfo nodeInfo2 = new NodeInfo("node-2", 9092);
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration("node-0", List.of(nodeInfo, nodeInfo1, nodeInfo2));
        NodeManager nodeManager = new NodeManager(clusterConfiguration, tempDir.resolve("node-0.data"));
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
         requestHandler = new RequestHandler(shardManager, clusterConfiguration, nodeManager);
    }

    @Test
    public void shouldRouteRequestToRemoteNode() throws IOException {
        Response actualResponsePUT = requestHandler.handleRequest("PUT|user:2|Sahil_1");
        Response actualResponsePUT1 = requestHandler.handleRequest("PUT|user:1|Sahil_1");
        Response actualResponseGET = requestHandler.handleRequest("GET|user:1");
        Response actualResponseDELETE = requestHandler.handleRequest("DELETE|user:1");
        Response actualResponse_GET_AFTER_DELETE = requestHandler.handleRequest("GET|user:1");
        Response expectedResponse = new Response(Response.Status.SUCCESS, "node-2", "operation successfull");
        Response expectedResponse1 = new Response(Response.Status.SUCCESS, "node-1", "operation successfull");
        Response expectedResponseGET = new Response(Response.Status.VALUE, "node-1", "Sahil_1");
        Response expectedResponseDELETE = new Response(Response.Status.VALUE, "node-1", "operation successfull");
        Response expectedResponseGET_AFTER_DELETE = new Response(Response.Status.VALUE, "node-1", "null");

        assertEquals(expectedResponse, actualResponsePUT);
        assertEquals(expectedResponse1, actualResponsePUT1);
        assertEquals(expectedResponseGET, actualResponseGET);
        assertEquals(expectedResponseDELETE, actualResponseDELETE);
        assertEquals(expectedResponseGET_AFTER_DELETE, actualResponse_GET_AFTER_DELETE);
    }

    @Test
    public void shouldTestLocalNodeExecution() throws IOException {
        Response actualResponse = requestHandler.handleRequest("PUT|user:0|Sahil_1");
        Response expectedResponse = new Response(Response.Status.SUCCESS, "node-0", "operation successfull");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldThrowExceptionforRemoteCallFailure() throws IOException {
       assertThrows(ConnectException.class, () -> requestHandler.handleRequest("PUT|user:2|Sahil_1"));
    }
}
