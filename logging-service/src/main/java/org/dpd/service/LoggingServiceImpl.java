package org.dpd.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderLogRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingServiceImpl implements LoggingService {

    @Value("${database.service.url}")
    private String databaseServiceUrl;

    private final WebClient webClient;

    @Override
    @CircuitBreaker(name = "orderLogService", fallbackMethod = "logToDatabaseFallback")
    public void logToDatabase(OrderLogRequest orderLogRequest) {
        webClient.post()
                .uri(databaseServiceUrl)
                .body(Mono.just(orderLogRequest), OrderLogRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Error calling Order Log Database Service")))
                .bodyToMono(String.class)
                .subscribe(response -> log.info("Order logged successfully"),
                        error -> log.error("Error occurred while calling Order Log Database Service", error));
    }

    public void logToDatabaseFallback(Throwable t) {
        log.error("Error occurred while calling Order Log Database Service. Falling back", t);
    }
}
