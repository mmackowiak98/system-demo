package org.dpd.orderlog.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dpd.orderlog.mapper.OrderLogMapper;
import org.dpd.orderlog.model.OrderLog;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.dpd.orderlog.repository.OrderLogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLogServiceImpl implements OrderLogService {

    private final OrderLogRepository orderLogRepository;
    private final OrderLogMapper orderLogMapper;

    @Override
    @Transactional
    @CircuitBreaker(name = "databaseService", fallbackMethod = "saveFallback")
    public OrderLogResponse save(OrderLogRequest orderLogRequest) {
        log.info("Saving order log");

        Optional<OrderLog> existingOrderLog =
                orderLogRepository.findByShipmentNumber(orderLogRequest.getShipmentNumber());

        if (existingOrderLog.isPresent()) {
            OrderLog updatedOrderLog = updateOrder(orderLogRequest, existingOrderLog.get());
            return orderLogMapper.toOrderLogResponse(updatedOrderLog);
        } else {
            OrderLog savedOrderLog = saveNewOrder(orderLogRequest);
            return orderLogMapper.toOrderLogResponse(savedOrderLog);
        }
    }
    public OrderLogResponse saveFallback(OrderLogRequest orderLogRequest, Throwable t) {
        log.error("Fallback method called due to exception: ", t);
        return null;
    }

    @Override
    @CircuitBreaker(name = "databaseService", fallbackMethod = "checkStatusCodeFallback")
    public Boolean checkStatusCode(String shipmentNumber, int statusCode) {
        log.info("Checking status code");

        return orderLogRepository.findByShipmentNumber(shipmentNumber)
                .map(orderLog -> orderLog.getStatusCode() != statusCode)
                .orElse(true);
    }
    public Boolean checkStatusCodeFallback(String shipmentNumber, int statusCode, Throwable t) {
        log.error("Fallback method called due to exception: ", t);
        return false;
    }

    private OrderLog updateOrder(OrderLogRequest orderRequest, OrderLog orderToUpdate) {
        log.info("Order already exists, checking status");
        if (orderToUpdate.getStatusCode() != orderRequest.getStatusCode()) {
            log.info("Status code is different, updating status");
            orderToUpdate.setStatusCode(orderRequest.getStatusCode());
            return orderLogRepository.save(orderToUpdate);
        } else {
            log.info("Status code is the same, no changes made");
            return orderToUpdate;
        }
    }

    private OrderLog saveNewOrder(OrderLogRequest orderRequest) {
        log.info("Creating new order");
        OrderLog mappedOrderLog = orderLogMapper.toOrderLog(orderRequest);
        return orderLogRepository.save(mappedOrderLog);
    }
}
