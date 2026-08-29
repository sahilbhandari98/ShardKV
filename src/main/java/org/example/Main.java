package org.example;

import network.ClusterConfiguration;
import network.RequestHandler;
import network.Response;
import node.NodeInfo;
import node.NodeManager;
import routing.ShardManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String nodeId = args[0];
        int port = Integer.parseInt(args[1]);
        String walName = args[2];
        ServerSocket serverSocket = new ServerSocket(port);

        List<NodeInfo> nodes = initializeCluster();
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration(nodeId, nodes);
        NodeManager nodeManager = new NodeManager(clusterConfiguration, Path.of("data", walName));
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
        RequestHandler requestHandler = new RequestHandler(shardManager, clusterConfiguration, nodeManager);

        System.out.println("Initialized node on port "+port);

        while(true) {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                String request;

                try {
                    while ((request = bufferedReader.readLine()) != null) {
                        try {
                            Response response = requestHandler.handleRequest(request);
                            bufferedWriter.write(response.getPayload() + "\n");
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

        }
    }
    public static List<NodeInfo> initializeCluster() {
        NodeInfo nodeInfo = new NodeInfo("node-0",9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1",9091);
        NodeInfo nodeInfo2 = new NodeInfo("node-2",9092);
        return List.of(nodeInfo, nodeInfo1, nodeInfo2);
    }
}