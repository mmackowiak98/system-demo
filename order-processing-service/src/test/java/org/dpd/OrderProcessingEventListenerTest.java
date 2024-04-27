package org.dpd;

import org.dpd.eventlistener.OrderProcessingEventListener;
import org.dpd.model.OrderRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class OrderProcessingEventListenerTest {

    @InjectMocks
    private OrderProcessingEventListener orderProcessingEventListener;

    @Mock
    private KafkaTemplate<String, OrderRequest> kafkaTemplate;
}
