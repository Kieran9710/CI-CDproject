package org.example.microassign1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private CustomerRepo customerRepo; // if needed

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetOrderById() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
        orderDTO.setItem("Book");
        orderDTO.setAmount(29.99);
        orderDTO.setOrderDate(LocalDate.now());
        orderDTO.setCustomerId(1L);

        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/orders/1"))
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.item").value("Book"))
                .andExpect(jsonPath("$.amount").value(29.99));
    }

    @Test
    void shouldGetOrdersByCustomerId() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
        orderDTO.setItem("Book");
        orderDTO.setAmount(29.99);
        orderDTO.setCustomerId(1L);

        when(orderService.getOrdersByCustomerId(1L)).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item").value("Book"));
    }

    @Test
    void shouldReturnNotFoundWhenOrderMissing() throws Exception {
        when(orderService.getOrderById(999L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + 1));

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

}
