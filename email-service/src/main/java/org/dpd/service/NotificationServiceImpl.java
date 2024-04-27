package org.dpd.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    @CircuitBreaker(name = "emailService", fallbackMethod = "sendEmailFallback")
    public void sendEmail(OrderMessage orderMessage) {
        log.info("Order: {}", orderMessage);
        log.info("Email sent to: {}", orderMessage.getReceiverEmail());
    }

    public void sendEmailFallback(OrderMessage orderMessage, Throwable t) {
        log.error("Error occurred while sending email. Falling back", t);
    }
}
