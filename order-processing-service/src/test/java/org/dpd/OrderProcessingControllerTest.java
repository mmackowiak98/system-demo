package org.dpd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dpd.controller.OrderProcessingController;
import org.dpd.model.OrderRequest;
import org.dpd.service.OrderProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderProcessingController.class)
public class OrderProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderProcessingService orderProcessingService;

    @Test
    public void testProcessOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiverCountryCode("US");
        orderRequest.setReceiverEmail("email@email.com");
        orderRequest.setStatusCode(20);
        orderRequest.setShipmentNumber("SH12312");
        orderRequest.setSenderCountryCode("US");

        doNothing().when(orderProcessingService).processOrder(orderRequest);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testProcessOrderValidationFailed() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiverCountryCode("US");
        orderRequest.setReceiverEmail("email@email.com");
        orderRequest.setShipmentNumber("SH12312");
        orderRequest.setStatusCode(150);
        orderRequest.setSenderCountryCode("US");

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("statusCode must be less than or equal to 100")));
    }
}