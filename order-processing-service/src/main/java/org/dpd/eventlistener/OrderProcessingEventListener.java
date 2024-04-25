package org.dpd.eventlistener;

import lombok.RequiredArgsConstructor;
import org.dpd.model.OrderProcessingEvent;
import org.dpd.model.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:kafka-config.properties")
public class OrderProcessingEventListener {

    @Value("${topic.name}")
    private String topic;

    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @EventListener
    public void handleOrderSaved(OrderProcessingEvent event) {
        OrderRequest order = event.getOrder();
        kafkaTemplate.send(topic, order.getShipmentNumber(), order);
    }
}
