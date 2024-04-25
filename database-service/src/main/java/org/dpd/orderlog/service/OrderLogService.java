package org.dpd.orderlog.service;


import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;

public interface OrderLogService {

    OrderLogResponse save(OrderLogRequest orderLog);

    Boolean checkStatusCode(String shipmentNumber, int statusCode);
}
