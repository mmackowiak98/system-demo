package org.dpd;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.dpd.model.OrderLogRequest;
import org.dpd.service.LoggingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@SpringBootTest(classes = LoggingServiceApplication.class)
public class LoggingServiceImplTest {

    private MockWebServer mockWebServer;
    private LoggingServiceImpl loggingService;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.create(mockWebServer.url("/").toString());
        loggingService = new LoggingServiceImpl(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testLogToDatabase() {
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("{\"message\":\"Order logged successfully\"}")
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        OrderLogRequest orderLogRequest = new OrderLogRequest();
        loggingService.logToDatabase(orderLogRequest);
    }
}
