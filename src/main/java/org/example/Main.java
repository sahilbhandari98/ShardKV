package org.example;

import KVStore.KVStore;
import KVStore.KvStoreFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        KVStore<String, String> cache = KvStoreFactory.getKvStoreFactory();
        cache.put("123","sahil");
        cache.put("234","rahul");
        cache.delete("234");
        System.out.println(cache.get("123"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            try {
                cache.put("123","sahil_thread");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).get();
        executorService.shutdown();

        System.out.println(cache.get("123"));

        testSplit();
    }

    public static void testSplit() {
        String str = "PUT|user:1|Sahil\n"+"PUT|user:2|Rahil\n"+"PUT|user:3|Ajay\n";
        boolean endWithNewLine = str.endsWith("\n");
        String[] records = str.split("\n",-1);

        for(int i=0;i< records.length;i++) {
            String record = records[i];
            if(i == records.length - 1 && !endWithNewLine) {
                System.out.println("Ignoring partially written record");
            }
            System.out.println(record);
        }
    }
}