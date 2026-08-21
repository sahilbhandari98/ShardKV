package org.example;


import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.RequestHandler;
import network.Response;
import node.KVNode;
import routing.ShardManager;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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

        KVNode node0 = new KVNode("node-0", new PersistedKVStore(new FileWriteAheadLog(Path.of("data","wal0.log"))));
        KVNode node1 = new KVNode("node-1", new PersistedKVStore(new FileWriteAheadLog(Path.of("data","wal1.log"))));
        KVNode node2 = new KVNode("node-2", new PersistedKVStore(new FileWriteAheadLog(Path.of("data","wal2.log"))));

        ShardManager shardManager = new ShardManager(3, List.of(node0, node1, node2));

        RequestHandler requestHandler = new RequestHandler(shardManager);

        Response response = requestHandler.handleRequest("PUT|user:1|Sahil");
        Response response1 = requestHandler.handleRequest("PUT|user:2|Rahul");
        Response response2 = requestHandler.handleRequest("PUT|user::39|Ajay");
        System.out.println(response.getPayload() +" served by shard - "+ response.getShard());
        System.out.println(response1.getPayload() +" served by shard - "+ response1.getShard());
        System.out.println(response2.getPayload() +" served by shard - "+ response2.getShard());
    }
}