package org.dpd;

import org.dpd.orderlog.mapper.OrderLogMapper;
import org.dpd.orderlog.model.OrderLog;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.dpd.orderlog.repository.OrderLogRepository;
import org.dpd.orderlog.service.OrderLogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderLogServiceImplTest {

    @InjectMocks
    private OrderLogServiceImpl orderLogService;

    @Mock
    private OrderLogRepository orderLogRepository;

    @Mock
    private OrderLogMapper orderLogMapper;

    @Test
    public void testSave() {
        OrderLogRequest orderLogRequest = new OrderLogRequest();
        OrderLog orderLog = new OrderLog();
        OrderLogResponse orderLogResponse = new OrderLogResponse();

        when(orderLogRepository.findByShipmentNumber(anyString())).thenReturn(Optional.empty());
        when(orderLogMapper.toOrderLog(any(OrderLogRequest.class))).thenReturn(orderLog);
        when(orderLogRepository.save(any(OrderLog.class))).thenReturn(orderLog);
        when(orderLogMapper.toOrderLogResponse(any(OrderLog.class))).thenReturn(orderLogResponse);

        OrderLogResponse save = orderLogService.save(orderLogRequest);
        Assertions.assertEquals(orderLogResponse, save);

        verify(orderLogMapper, times(1)).toOrderLog(any(OrderLogRequest.class));
        verify(orderLogRepository, times(1)).save(any(OrderLog.class));
        verify(orderLogMapper, times(1)).toOrderLogResponse(any(OrderLog.class));
    }

    @Test
    public void testCheckStatusCodeNotTheSame() {
        OrderLog orderLog = new OrderLog();
        orderLog.setStatusCode(25);
        when(orderLogRepository.findByShipmentNumber(anyString())).thenReturn(Optional.of(orderLog));

        Boolean checkedStatusCode = orderLogService.checkStatusCode("123", 50);

        Assertions.assertEquals(true, checkedStatusCode);
    }

    @Test
    public void testCheckStatusCodeTheSame() {
        OrderLog orderLog = new OrderLog();
        orderLog.setStatusCode(25);
        when(orderLogRepository.findByShipmentNumber(anyString())).thenReturn(Optional.of(orderLog));

        Boolean checkedStatusCode = orderLogService.checkStatusCode("123", 25);

        Assertions.assertEquals(false, checkedStatusCode);
    }
}
