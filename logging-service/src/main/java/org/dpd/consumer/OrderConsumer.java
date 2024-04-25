package org.dpd.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderLogRequest;
import org.dpd.service.LoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final LoggingService loggingService;
    private final ReceiverOptions<String, String> receiverOptions;
    @Value("${retry.count}")
    private int retryCount;
    @Value("${backpressure.buffer}")
    private int backpressureBuffer;

    public void consume(){
        Flux<ReceiverRecord<String, String>> kafkaFlux = KafkaReceiver.create(receiverOptions).receive();
        kafkaFlux
                .onBackpressureBuffer(backpressureBuffer)
                .retry(retryCount)
                .subscribe(record -> {
                    try {
                        log.info("Consumed message: {}", record.value());
                        OrderLogRequest orderLogRequest = objectMapper.readValue(record.value(), OrderLogRequest.class);
                        loggingService.logToDatabase(orderLogRequest);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
