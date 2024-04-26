package org.dpd.orderlog.service;

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

    @Override
    public Boolean checkStatusCode(String shipmentNumber, int statusCode) {
        log.info("Checking status code");

        return orderLogRepository.findByShipmentNumber(shipmentNumber)
                .map(orderLog -> orderLog.getStatusCode() == statusCode)
                .orElse(true);
    }

    private OrderLog updateOrder(OrderLogRequest orderRequest, OrderLog orderToUpdate) {
        log.info("Order already exists, updating status");
        orderToUpdate.setStatusCode(orderRequest.getStatusCode());
        return orderLogRepository.save(orderToUpdate);
    }

    private OrderLog saveNewOrder(OrderLogRequest orderRequest) {
        log.info("Creating new order");
        OrderLog mappedOrderLog = orderLogMapper.toOrderLog(orderRequest);
        return orderLogRepository.save(mappedOrderLog);
    }
}
