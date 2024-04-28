package org.dpd.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderLogRequest;
import org.dpd.service.LoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer implements ApplicationRunner {

    private final ObjectMapper objectMapper;
    private final LoggingService loggingService;
    private final KafkaReceiver<String, String> consumer;

    @Value("${retry.count}")
    private int retryCount;
    @Value("${backpressure.buffer}")
    private int backpressureBuffer;

    @Override
    public void run(ApplicationArguments args) {
        log.info("OrderConsumer started");
        consume();
    }

    private void consume() {
        log.info("Consuming messages");
        consumer
                .receive()
                .onBackpressureBuffer(backpressureBuffer)
                .retry(retryCount)
                .subscribe(message -> {
                    try {
                        log.info("Consumed message: {}", message.value());
                        OrderLogRequest orderLogRequest = objectMapper.readValue(message.value(),
                                OrderLogRequest.class);
                        loggingService.logToDatabase(orderLogRequest);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
