package org.dpd.orderlog.controller;

import lombok.RequiredArgsConstructor;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.dpd.orderlog.service.OrderLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-log")
@RequiredArgsConstructor
public class OrderLogController {
    private final OrderLogService orderLogService;

    @PostMapping
    public ResponseEntity<OrderLogResponse> saveOrderLog(@RequestBody OrderLogRequest orderLogRequest) {
        return ResponseEntity.ok(orderLogService.save(orderLogRequest));
    }
}
