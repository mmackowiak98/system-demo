package org.dpd.service;

import org.dpd.model.OrderLogRequest;

public interface NotificationService {
    void sendEmail(OrderLogRequest orderLogRequest);
}
