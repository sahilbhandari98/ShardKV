package network;

import KVStore.KVStore;
import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MultiNodeIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldReplicateToReplicas() throws IOException, InterruptedException {


        Process node1 = startNode("node-0", "9090", tempDir.resolve("node-0.data").toAbsolutePath().toString());
        Process node2 = startNode("node-1", "9091", tempDir.resolve("node-1.data").toAbsolutePath().toString());
        Process node3 = startNode("node-2", "9092", tempDir.resolve("node-2.data").toAbsolutePath().toString());
        try {

            waitForPort(9090);
            waitForPort(9091);
            waitForPort(9092);

            String response = sendRequest(9090,"PUT|user:test|replication_key_value_test");
            assertEquals("operation successfull", response);

            String getResponse = sendRequest(9090,"GET|user:test");
            assertEquals("replication_key_value_test", getResponse);

            FileWriteAheadLog fileWriteAheadLog = new FileWriteAheadLog(tempDir.resolve("node-2.data"));
            PersistedKVStore<String, String> persistedKVStore = new PersistedKVStore<>(fileWriteAheadLog);
            String replicatedResponse = persistedKVStore.get("user:test");
            assertEquals("replication_key_value_test", replicatedResponse);

            String deleteResponse = sendRequest(9090,"DELETE|user:test");
            assertEquals("operation successfull", deleteResponse);

            String getResponseAfterDelete = sendRequest(9090,"GET|user:test");
            assertEquals("null", getResponseAfterDelete);
            // Re-read replica WAL to verify DELETE was replicated
            persistedKVStore.recovery();
            String replicatedDeleteResponse = persistedKVStore.get("user:test");
            assertNull(replicatedDeleteResponse);

        } finally {
            node1.destroy();
            node2.destroy();
            node3.destroy();
        }
    }

    public void waitForPort(int port) throws IOException, InterruptedException {
        long timeout = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < timeout) {
            try (Socket socket = new Socket("localhost", port)) {
                return;
            } catch (ConnectException connectException) {
                Thread.sleep(1000);
            }
        }
    }

    public String sendRequest(int port, String request) throws IOException {
        try (Socket socket = new Socket("localhost", port)) {

            System.out.println("write using output stream");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            bufferedWriter.write(request + "\n");
            bufferedWriter.flush();


            return bufferedReader.readLine();
        }
    }

    public Process startNode(String nodeId, String port, String walPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java",
                "-jar",
                "/Users/sahilbhandari/IdeaProjects/ShardKV/target/ShardKV-1.0-SNAPSHOT.jar",
                nodeId,
                port,
                walPath);

        return processBuilder.inheritIO().start();
    }
}
