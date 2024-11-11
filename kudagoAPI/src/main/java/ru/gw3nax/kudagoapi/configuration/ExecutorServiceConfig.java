package ru.gw3nax.kudagoapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ExecutorServiceConfig {


    private final int fixedThreadPoolSize;

    ExecutorServiceConfig(ApplicationConfig applicationConfig) {
        fixedThreadPoolSize = applicationConfig.fixedThreadPoolSize();
    }

    @Bean(name = "dataInitializationExecutor")
    public ExecutorService dataInitializationExecutor(){
        return Executors.newFixedThreadPool(fixedThreadPoolSize, new ThreadFactory() {
            private AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Data-Init-Thread-" + count.getAndIncrement());
            }
        });
    }

    @Bean(name = "dataScheduledExecutor")
    public ScheduledExecutorService dataScheduledExecutor() {
        return Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Data-Schedule-Thread-" + count.getAndIncrement());
            }
        });
    }
}
