package ru.gw3nax.kudagoapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.gw3nax.customstarter.PostProxy.PostProxy;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;
import ru.gw3nax.kudagoapi.exception.ServiceException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class DataInitializationService {

    private final ExecutorService fixedThreadPool;
    private final ScheduledExecutorService scheduledThreadPool;
    private final Duration duration;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final Semaphore semaphore;

    public DataInitializationService(
            @Qualifier("dataInitializationExecutor") ExecutorService fixedThreadPool,
            @Qualifier("dataScheduledExecutor") ScheduledExecutorService scheduledThreadPool,
            ApplicationConfig applicationConfig,
            CategoryService categoryService,
            LocationService locationService
    ) {
        this.fixedThreadPool = fixedThreadPool;
        this.scheduledThreadPool = scheduledThreadPool;
        this.duration = applicationConfig.duration();
        this.categoryService = categoryService;
        this.locationService = locationService;
        this.semaphore = new Semaphore(applicationConfig.maxRequests());
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted() {
        scheduledThreadPool.scheduleAtFixedRate(this::initializeData, duration.toSeconds(), duration.toSeconds(), TimeUnit.SECONDS);
    }

    @PostProxy
    @Profiling
    public void initializeData() {
        List<CompletableFuture<Void>> futures = Arrays.asList(
                CompletableFuture.runAsync(this::initializeCategories, fixedThreadPool),
                CompletableFuture.runAsync(this::initializeLocations, fixedThreadPool)
        );

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            throw new ServiceException("Ошибка при инициализации данных", 500);
        }


        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            throw new ServiceException("Ошибка при инициализации данных", 500);
        }

    }

    private void initializeCategories() {
        try {
            semaphore.acquire();
            categoryService.init();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    private void initializeLocations() {
        try {
            semaphore.acquire();
            locationService.init();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }
}
