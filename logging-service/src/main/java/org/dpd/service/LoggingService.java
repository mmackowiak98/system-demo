package org.dpd.service;

import org.dpd.model.OrderLogRequest;

public interface LoggingService {

    void logToDatabase(OrderLogRequest orderLogRequest);
}
