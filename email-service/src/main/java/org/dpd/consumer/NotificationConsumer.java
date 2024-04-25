package org.dpd.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderLogRequest;
import org.dpd.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final ReceiverOptions<String, String> receiverOptions;
    @Value("${retry.count}")
    private int retryCount;
    @Value("${backpressure.buffer}")
    private int backpressureBuffer;

    public void consume() {
    Flux<ReceiverRecord<String, String>> kafkaFlux = KafkaReceiver.create(receiverOptions).receive();
    kafkaFlux
        .onBackpressureBuffer(backpressureBuffer)
        .retry(retryCount)
        .filter(record -> hasStatusCodeChanged(record.value()))
        .subscribe(record -> {
            try {
                log.info("Consumed message: {}", record.value());
                OrderLogRequest orderLogRequest = objectMapper.readValue(record.value(), OrderLogRequest.class);
                notificationService.sendEmail(orderLogRequest);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean hasStatusCodeChanged(String message) {
        //TODO 2: Implement the logic to check if the status code has changed based on response from database-service
        return true;
    }
}
