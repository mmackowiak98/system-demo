package org.dpd.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderProcessingEvent;
import org.dpd.model.OrderRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @CircuitBreaker(name = "orderProcessingService", fallbackMethod = "fallback")
    public void processOrder(OrderRequest orderRequest) {
        log.info("Processing order");
        eventPublisher.publishEvent(new OrderProcessingEvent(this,orderRequest));
    }

    public void fallback(Throwable t) {
        log.error("Fallback method called due to exception: ", t);
    }
}
