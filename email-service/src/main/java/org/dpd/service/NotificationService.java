package org.dpd.service;

import org.dpd.model.OrderMessage;

public interface NotificationService {
    void sendEmail(OrderMessage orderMessage);
}
