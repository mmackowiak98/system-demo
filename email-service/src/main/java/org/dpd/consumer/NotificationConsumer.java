package org.dpd.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderMessage;
import org.dpd.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer implements ApplicationRunner {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final KafkaReceiver<String, String> consumer;
    @Value("${retry.count}")
    private int retryCount;
    @Value("${backpressure.buffer}")
    private int backpressureBuffer;

    @Override
    public void run(ApplicationArguments args)  {
        consume();
    }

    private void consume() {
    consumer
        .receive()
        .onBackpressureBuffer(backpressureBuffer)
        .retry(retryCount)
        .filter(message -> hasStatusCodeChanged(message.value()))
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

    private boolean hasStatusCodeChanged(String message) {
        //TODO 2: Implement the logic to check if the status code has changed based on response from database-service
        return true;
    }
}
