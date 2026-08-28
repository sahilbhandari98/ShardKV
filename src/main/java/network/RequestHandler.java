package network;

import KVStore.WAL.Operation;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;
import routing.ShardManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static network.Response.success;
import static network.Response.value;

public class RequestHandler {

    ShardManager shardManager;
    ClusterConfiguration clusterConfiguration;
    NodeManager nodeManager;

    public RequestHandler(ShardManager shardManager, ClusterConfiguration clusterConfiguration, NodeManager nodeManager) {
        this.shardManager = shardManager;
        this.clusterConfiguration = clusterConfiguration;
        this.nodeManager = nodeManager;
    }
    public Response handleRequest(String request) throws IOException {
        System.out.println("raw request "+request);
        Request req = RequestParser.requestParser(request);
        System.out.println(req.getOperation()+" :: "+req.getKey());
        NodeInfo node = shardManager.getNode(req.getKey());
        Response response;
        if(node.getNodeId().equals(clusterConfiguration.getCurrentNodeId())) {
            response = executeLocally(req, nodeManager.getKvNode());
        } else {
            response = remoteCall(req, node);
        }
        return response;
    }

    public Response executeLocally(Request req, KVNode node) throws IOException {
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

    public Response remoteCall( Request req, NodeInfo node) throws IOException {

        Socket socket = new Socket("localhost", node.getPort());
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if(Operation.PUT.equals(req.getOperation())) {
            bufferedWriter.write(req.getOperation().name() + "|" + req.getKey() + "|" + req.getValue() + "\n");
        } else {
            bufferedWriter.write(req.getOperation().name() + "|" + req.getKey() + "\n");
        }
        bufferedWriter.flush();


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String remoteCallResponse = bufferedReader.readLine();
        System.out.println(remoteCallResponse);
        return Response.success(remoteCallResponse);
    }
}
