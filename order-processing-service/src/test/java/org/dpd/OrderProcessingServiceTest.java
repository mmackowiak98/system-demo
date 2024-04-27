package org.dpd;

import org.dpd.model.OrderProcessingEvent;
import org.dpd.model.OrderRequest;
import org.dpd.service.OrderProcessingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderProcessingServiceTest {

    @InjectMocks
    private OrderProcessingServiceImpl orderProcessingService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void testProcessOrder() {
        OrderRequest orderRequest = new OrderRequest();

        orderProcessingService.processOrder(orderRequest);

        verify(eventPublisher, times(1)).publishEvent(any(OrderProcessingEvent.class));
    }
}
