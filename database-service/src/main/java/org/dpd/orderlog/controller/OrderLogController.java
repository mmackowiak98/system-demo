package org.dpd.orderlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.dpd.orderlog.service.OrderLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order-log")
@RequiredArgsConstructor
public class OrderLogController {
    private final OrderLogService orderLogService;

    @PostMapping
    public ResponseEntity<OrderLogResponse> save(@RequestBody OrderLogRequest orderLogRequest) {
        log.trace("Received order log request: {}", orderLogRequest);
        return ResponseEntity.ok(orderLogService.save(orderLogRequest));
    }

    public ResponseEntity<Boolean> checkStatusCode(@RequestParam String shipmentNumber, @RequestParam int statusCode) {
        log.trace("Received status code check request: {}", statusCode);
        return ResponseEntity.ok(orderLogService.checkStatusCode(shipmentNumber,statusCode));
    }
}
