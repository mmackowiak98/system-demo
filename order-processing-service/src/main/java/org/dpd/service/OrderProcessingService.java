package org.dpd.service;

import org.dpd.model.OrderRequest;

public interface OrderProcessingService {
    void processOrder(OrderRequest orderRequest);
}
