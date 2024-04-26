package org.dpd.eventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderProcessingEvent;
import org.dpd.model.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:kafka-config.properties")
public class RetryOrderProcessingEventListener {

  @Value("${retry-topic.name}")
  private String topic;

  private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

  @EventListener
  public void processEvent(OrderProcessingEvent event) {
    log.info("Retrying processing event");
    OrderRequest order = event.getOrder();
    log.info("Sending order to Kafka topic: {}", topic);
    kafkaTemplate.send(topic, order.getShipmentNumber(), order);
  }
}
