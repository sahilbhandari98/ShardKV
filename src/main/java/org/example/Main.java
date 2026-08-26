package org.example;


import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;
import network.RequestHandler;
import network.Response;
import node.KVNode;
import node.NodeInfo;
import node.NodeManager;
import routing.ShardManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
//        String node = args[0];
//        int port = Integer.parseInt(args[1]);
//        String path = args[2];
//
//        ServerSocket serverSocket = new ServerSocket(port);

//        while(true) {
//            System.out.println("start recieveing data");
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String request;
//            while((request = bufferedReader.readLine()) != null) {
//                System.out.println(request);
//            }
//            socket.close();
//            //System.out.println(request);
//        }


//        String caller = args[0];
//        if("A".equalsIgnoreCase(caller)) {
//            serverA(args);
//        } else if("B".equalsIgnoreCase(caller)) {
//            serverB(args);
//        }
        String nodeId = args[0];
        int port = Integer.parseInt(args[1]);
        String walName = args[2];
        ServerSocket serverSocket = new ServerSocket(port);

        List<NodeInfo> nodes = initializeCluster();
        ClusterConfiguration clusterConfiguration = new ClusterConfiguration(nodeId, nodes);
        NodeManager nodeManager = new NodeManager(clusterConfiguration, walName);
        ShardManager shardManager = new ShardManager(nodeManager, clusterConfiguration);
        RequestHandler requestHandler = new RequestHandler(shardManager, clusterConfiguration, nodeManager);

        System.out.println("Initilized node on port "+port);

        while(true) {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Response response = requestHandler.handleRequest(bufferedReader.readLine());
            System.out.println(response.getPayload());

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(response.getPayload()+"\n");
            bufferedWriter.flush();
        }


    }
    public static List<NodeInfo> initializeCluster() {
        NodeInfo nodeInfo = new NodeInfo("node-0",9090);
        NodeInfo nodeInfo1 = new NodeInfo("node-1",9091);
        NodeInfo nodeInfo2 = new NodeInfo("node-2",9092);
        return List.of(nodeInfo, nodeInfo1, nodeInfo2);
    }
    public static void serverA(String[] args) throws IOException {
        String node = args[1];
        int port = Integer.parseInt(args[2]);
        String path = args[3];

        ServerSocket serverSocket = new ServerSocket(port);

        while(true) {
            Socket socket = serverSocket.accept();
            Socket targetSocket = new Socket("localhost",9091);
            System.out.println("Request Router");
            BufferedReader inputSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader targetSocketReader = new BufferedReader(new InputStreamReader(targetSocket.getInputStream()));
            BufferedWriter inputSocketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedWriter targetSocketWriter = new BufferedWriter(new OutputStreamWriter(targetSocket.getOutputStream()));

            String request = inputSocketReader.readLine();

            System.out.println("client A recieved from nc: "+request);
            targetSocketWriter.write(request+"\n");
            targetSocketWriter.flush();


            String response = targetSocketReader.readLine();
            System.out.println("client A recieved from B: "+response);

            inputSocketWriter.write("client A write to nc: "+response);
            inputSocketWriter.flush();
        }
    }

    public static void serverB(String[] args) throws IOException {
        String node = args[1];
        int port = Integer.parseInt(args[2]);
        String path = args[3];

        ServerSocket serverSocket = new ServerSocket(port);

        while(true) {
            Socket socket = serverSocket.accept();
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while((input = socketReader.readLine()) != null) {
                System.out.println("client B recieved from A: "+input);
                socketWriter.write("success\n");
                socketWriter.flush();
            }

        }
    }
}