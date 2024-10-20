package ru.gw3nax.kudagoapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;

import java.util.concurrent.*;

import static org.mockito.Mockito.*;

class DataInitializationServiceTest {

    @Mock
    private ExecutorService fixedThreadPool;

    @Mock
    private ScheduledExecutorService scheduledThreadPool;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private DataInitializationService dataInitializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationConfig.duration()).thenReturn(3600L);
        when(applicationConfig.maxRequests()).thenReturn(2);
    }

    @Test
    void testInitializeData_withSemaphoreLimit() {
        Future<?> mockFuture = mock(Future.class);
        ScheduledFuture<?> mockScheduledFuture = mock(ScheduledFuture.class);

        doReturn(mockFuture).when(fixedThreadPool).submit(any(Runnable.class));
        doReturn(mockScheduledFuture).when(scheduledThreadPool).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        dataInitializationService.onApplicationStarted();
        dataInitializationService.initializeData();

        verify(fixedThreadPool, times(2)).submit(any(Runnable.class));
    }
}

