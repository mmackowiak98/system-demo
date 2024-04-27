package org.dpd.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderMessage;
import org.dpd.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer implements ApplicationRunner {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final KafkaReceiver<String, String> consumer;
    @Value("${database.service.host}")
    private String databaseServiceHost;
    @Value("${retry.count}")
    private int retryCount;
    @Value("${backpressure.buffer}")
    private int backpressureBuffer;

    @Override
    public void run(ApplicationArguments args) {
        consume();
    }

    public void consume() {
        consumer
                .receive()
                .onBackpressureBuffer(backpressureBuffer)
                .retry(retryCount)
                .flatMap(message -> hasStatusCodeChanged(message.value())
                        .filter(Boolean::booleanValue)
                        .map(response -> message))
                .subscribe(message -> {
                    try {
                        log.info("Consumed message: {}", message.value());
                        OrderMessage orderMessage = objectMapper.readValue(message.value(), OrderMessage.class);
                        notificationService.sendEmail(orderMessage);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Mono<Boolean> hasStatusCodeChanged(String message) {
        try {
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host(databaseServiceHost)
                            .port(8083)
                            .path("/order-log")
                            .queryParam("shipmentNumber", orderMessage.getShipmentNumber())
                            .queryParam("statusCode", orderMessage.getStatusCode())
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .doOnNext(response -> {
                        if (response) {
                            log.info("Status code has changed");
                        } else {
                            log.info("Status code has not changed");
                        }
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
