package com.social.network.users.loadTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class LoadTest {

    public static final String url = "https://go.skillbox.ru/";

    @Test
    void loadTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RestTemplate restTemplate = new RestTemplate();

        List<Future<?>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(executorService.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                }
            }));
        }

        for (Future<?> future : list) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        shutDownExecutorService(executorService);
        Assertions.assertTrue(executorService.isShutdown());
    }

    private void shutDownExecutorService(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}
