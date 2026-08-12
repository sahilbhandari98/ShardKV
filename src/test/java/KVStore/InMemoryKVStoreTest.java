package KVStore;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryKVStoreTest {

    @Test
    public void shouldStoreAndRetrieveValue() throws IOException {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory();
        store.put("user:1", "Sahil");

        assertEquals("Sahil", store.get("user:1"));
    }

    @Test
    public void shouldOverwriteExistingValue() throws IOException {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory();
        store.put("user:1", "Sahil");
        store.put("user:1", "Rahul");

        assertEquals("Rahul", store.get("user:1"));
    }

    @Test
    public void shouldDeleteValue() {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory();
        assertNull(store.get("user:1"));
    }

    @Test
    public void shouldDeleteMissingValue() throws IOException {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory();
        store.put("user:1", "Sahil");
        store.delete("user:2");

        assertEquals("Sahil", store.get("user:1"));
        assertNull(store.get("user:2"));
    }

    @Test
    public void shouldTestConcurrentAccess() throws ExecutionException, InterruptedException {
        KVStore<String, String> store = KvStoreFactory.getKvStoreFactory();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for(int threadId=0;threadId<10;threadId++){
            final int id = threadId;
            futures.add(executorService.submit(() -> {
                for (int i = 0; i < 100; i++) {
                    int key = id * 100 + i;
                    try {
                        store.put("user:"+key, "value:"+key);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }

        for(Future<?> future: futures) {
            future.get();
        }
        executorService.shutdown();

        for(int i=0;i<1000;i++) {
            assertEquals("value:"+i,store.get("user:"+i));
        }

    }
}
