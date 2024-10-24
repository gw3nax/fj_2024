package ru.gw3nax.currency_exchanger.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gw3nax.currency_exchanger.config.ApplicationConfig;
import ru.gw3nax.currency_exchanger.exceptions.CBRUnavailableException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CBRClientTest {

    @Mock
    ApplicationConfig applicationConfig;
    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private CBRClient cbrClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cbrClient.BASE_URL = "http://example.com/";
    }


    @Test
    void getAllCurrenciesFromCB_ShouldThrowServiceException_WhenIOExceptionOccurs() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("Exception occurred"));

        CBRUnavailableException exception = assertThrows(CBRUnavailableException.class, () -> {
            cbrClient.getCurrencies();
        });

        assertEquals(exception.getHeaders().get("Retry-After"), "3600");
    }
}
