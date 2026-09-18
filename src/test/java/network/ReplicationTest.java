package network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplicationTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldReplicateToReplicas() throws IOException {


        Process node1 = startNode("node-0", "9090", tempDir.resolve("node-0.data").toAbsolutePath().toString());
        Process node2 = startNode("node-1", "9091", tempDir.resolve("node-1.data").toAbsolutePath().toString());
        Process node3 = startNode("node-2", "9092", tempDir.resolve("node-2.data").toAbsolutePath().toString());
        try {
            System.out.println("send requests for integration tests");
            String response = sendRequest("PUT|user:test|replication_test_value");
            assertEquals("operation successfull", response);

            String getResponse = sendRequest("GET|user:test");
            assertEquals("replication_test_value", getResponse);
        } finally {
            node1.destroy();
            node2.destroy();
            node3.destroy();
        }
    }

    public String sendRequest(String request) throws IOException {
        System.out.println("start on port 9090");
        Socket socket = new Socket("localhost",9090);

        System.out.println("write using output stream");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        bufferedWriter.write(request+"\n");
        bufferedWriter.flush();


        return  bufferedReader.readLine();
    }

    public Process startNode(String nodeId, String port, String walPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java",
                "-jar",
                "/Users/sahilbhandari/IdeaProjects/ShardKV/target/ShardKV-1.0-SNAPSHOT.jar",
                nodeId,
                port,
                walPath);

        return processBuilder.start();
    }
}
