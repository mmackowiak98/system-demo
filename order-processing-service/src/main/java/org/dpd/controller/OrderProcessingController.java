package org.dpd.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpd.model.OrderRequest;
import org.dpd.service.OrderProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderProcessingController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping
    public ResponseEntity<Void> processOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("Received order request");
        orderProcessingService.processOrder(orderRequest);
        return ResponseEntity.ok().build();
    }
}
