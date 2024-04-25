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
        log.trace("Saving order log");

        Optional<OrderLog> existingOrder =
                orderLogRepository.findByShipmentNumber(orderLogRequest.getShipmentNumber());

        if (existingOrder.isPresent()) {
            OrderLog updatedOrderLog = updateOrder(orderLogRequest, existingOrder.get());
            orderLogMapper.toOrderLogResponse(updatedOrderLog);
        } else {
            OrderLog savedOrderLog = saveNewOrder(orderLogRequest);
            return orderLogMapper.toOrderLogResponse(savedOrderLog);
        }
        throw new RuntimeException("Error saving order log");
    }

    private OrderLog updateOrder(OrderLogRequest orderRequest, OrderLog orderToUpdate) {
        log.trace("Order already exists, updating status");
        orderToUpdate.setStatusCode(orderRequest.getStatusCode());
        return orderLogRepository.save(orderToUpdate);
    }

    private OrderLog saveNewOrder(OrderLogRequest orderRequest) {
        log.trace("Creating new order");
        OrderLog mappedOrderLog = orderLogMapper.toOrderLog(orderRequest);
        return orderLogRepository.save(mappedOrderLog);
    }
}
