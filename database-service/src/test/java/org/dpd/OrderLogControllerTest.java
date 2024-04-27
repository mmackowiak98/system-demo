package org.dpd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dpd.orderlog.controller.OrderLogController;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.dpd.orderlog.service.OrderLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderLogController.class)
public class OrderLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderLogService orderLogService;

    @Test
    public void testSave() throws Exception {
        OrderLogRequest request = new OrderLogRequest();
        OrderLogResponse response = new OrderLogResponse();

        when(orderLogService.save(request)).thenReturn(response);

        mockMvc.perform(post("/order-log")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckStatusCode() throws Exception {
        String shipmentNumber = "testShipmentNumber";
        int statusCode = 200;
        Boolean response = true;

        when(orderLogService.checkStatusCode(shipmentNumber, statusCode)).thenReturn(response);

        mockMvc.perform(get("/order-log")
                .param("shipmentNumber", shipmentNumber)
                .param("statusCode", String.valueOf(statusCode)))
                .andExpect(status().isOk())
                .andExpect(content().string(response.toString()));
    }

    @Test
    public void testSaveWithoutBody() throws Exception {
        mockMvc.perform(post("/order-log")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testCheckStatusCodeWithoutParameters() throws Exception {
        mockMvc.perform(get("/order-log"))
                .andExpect(status().is5xxServerError());
    }
}