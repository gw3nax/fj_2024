package ru.gw3nax.kudagoapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.gw3nax.customstarter.PostProxy.PostProxy;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoapi.command.InitCommand;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;
import ru.gw3nax.kudagoapi.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class DataInitializationService {

    private final List<InitCommand> initCommands;
    private final ScheduledExecutorService scheduledThreadPool;
    private final ExecutorService fixedThreadPool;
    private final Long duration;
    private final Semaphore semaphore;

    public DataInitializationService(
            @Qualifier("dataScheduledExecutor") ScheduledExecutorService scheduledThreadPool,
            ApplicationConfig applicationConfig,
            List<InitCommand> initCommands,
            @Qualifier("dataInitializationExecutor") ExecutorService fixedThreadPool
    ) {
        this.fixedThreadPool = fixedThreadPool;
        this.initCommands = initCommands;
        this.scheduledThreadPool = scheduledThreadPool;
        this.duration = applicationConfig.duration();
        this.semaphore = new Semaphore(applicationConfig.maxRequests());
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted() {
        scheduledThreadPool.scheduleAtFixedRate(this::initializeData, duration,
                duration, TimeUnit.SECONDS);
    }

    @PostProxy
    @Profiling
    public void initializeData() {
        List<Future<?>> futures = new ArrayList<>();
        futures.add(fixedThreadPool.submit(this::init));
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new ServiceException("Ошибка при инициализации данных", 500);
            }
        }
    }

    private void init() {
        try {
            semaphore.acquire();
            for (InitCommand initCommand : initCommands) {
                initCommand.execute();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

}
