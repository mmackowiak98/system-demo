package org.dpd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderLogRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendEmail(OrderLogRequest orderLogRequest) {
        log.info("Order: {}", orderLogRequest.getReceiverEmail());
        log.info("Email sent to: {}", orderLogRequest.getReceiverEmail());
    }
}
