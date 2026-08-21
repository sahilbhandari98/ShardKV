package org.example;


import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;


public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        String node = args[0];
        int port = Integer.parseInt(args[1]);
        String path = args[2];

        ServerSocket serverSocket = new ServerSocket(port);

        while(true) {
            System.out.println("start recieveing data");
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String request;
            while((request = bufferedReader.readLine()) != null) {
                System.out.println(request);
            }
            socket.close();
            //System.out.println(request);
        }
    }
}