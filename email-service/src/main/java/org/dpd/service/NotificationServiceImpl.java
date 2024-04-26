package org.dpd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendEmail(OrderMessage orderMessage) {
        log.info("Order: {}", orderMessage);
        log.info("Email sent to: {}", orderMessage.getReceiverEmail());
    }
}
