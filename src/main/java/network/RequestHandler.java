package network;

import node.KVNode;
import routing.ShardManager;

import java.io.IOException;

import static network.Response.success;
import static network.Response.value;

public class RequestHandler {

    ShardManager shardManager;

    public RequestHandler(ShardManager shardManager) {
        this.shardManager = shardManager;
    }
    public Response handleRequest(String request) throws IOException {
        Request req = RequestParser.requestParser(request);
        KVNode node = shardManager.getNode(req.getKey());
        return switch (req.getOperation()) {
            case PUT -> {
                System.out.println(req.getKey());
                node.put(req.getKey(), req.getValue());
                yield success(node.getNodeID());
            }
            case GET -> {
                String result = node.get(req.getKey());
                yield value(node.getNodeID(), result);
            }
            case DELETE -> {
                node.delete(req.getKey());
                yield success(node.getNodeID());
            }
        };
    }
}
