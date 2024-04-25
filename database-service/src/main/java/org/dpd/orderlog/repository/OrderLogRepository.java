package org.dpd.orderlog.repository;


import org.dpd.orderlog.model.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {

    Optional<OrderLog> findByShipmentNumber(String shipmentNumber);
}
