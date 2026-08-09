package org.example;

import KVStore.KVStore;
import KVStore.KvStoreFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KVStore<String, String> cache = KvStoreFactory.getKvStoreFactory();
        cache.put("123","sahil");
        cache.put("234","rahul");
        cache.delete("234");
        System.out.println(cache.get("123"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> cache.put("123","sahil_thread")).get();
        executorService.shutdown();

        System.out.println(cache.get("123"));
    }
}