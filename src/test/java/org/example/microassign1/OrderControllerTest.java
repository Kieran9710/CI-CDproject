package org.example.microassign1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderController orderController;

    private Order sampleOrder;
    private OrderDTO sampleOrderDTO;
    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("John Doe");
        sampleCustomer.setEmail("john@example.com");

        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setItem("Book");
        sampleOrder.setAmount(29.99);
        sampleOrder.setOrderDate(LocalDate.now());
        sampleOrder.setCustomer(sampleCustomer);

        sampleOrderDTO = new OrderDTO(sampleOrder, sampleCustomer);
    }

    // Test GET all orders
    @Test
    void testGetAllOrders() {
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = new PageImpl<>(List.of(sampleOrder));

        when(orderService.getAllOrders(pageable)).thenReturn(orderPage);

        Page<OrderDTO> result = orderController.getAllOrders(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Book", result.getContent().get(0).getItem());
        assertEquals(29.99, result.getContent().get(0).getAmount());
    }

    // Test GET order by ID
    @Test
    void testGetOrderById() {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrderDTO);

        OrderDTO result = orderController.getOrder(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("Book", result.getItem());
        assertEquals(29.99, result.getAmount());
    }

    // Test GET orders by Customer ID
    @Test
    void testGetOrdersByCustomerId() {
        when(orderService.getOrdersByCustomerId(1L)).thenReturn(List.of(sampleOrderDTO));

        List<OrderDTO> result = orderController.getOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book", result.get(0).getItem());
    }

    // Test POST create order
    @Test
    void testCreateOrder() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(orderService.saveOrder(any(Order.class))).thenReturn(sampleOrder);

        OrderDTO result = orderController.createOrder(sampleOrderDTO);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("Book", result.getItem());
        assertEquals(29.99, result.getAmount());
    }

    // Test GET orders by date range
    @Test
    void testGetOrdersByDateRange() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = new PageImpl<>(List.of(sampleOrder));

        when(orderRepo.findByOrderDateBetween(startDate, endDate, pageable)).thenReturn(orderPage);

        Page<OrderDTO> result = orderController.getOrdersByDateRange(startDate, endDate, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Book", result.getContent().get(0).getItem());
    }

    // Test DELETE order
    @Test
    void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(1L);

        orderController.deleteOrder(1L);

        verify(orderService, times(1)).deleteOrder(1L);
    }

    // Test POST create order with customer not found
    @Test
    void testCreateOrder_CustomerNotFound() {
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderController.createOrder(sampleOrderDTO);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());  // Use getStatusCode() to get the HttpStatus
        assertEquals("Customer not found with ID: 1", exception.getReason());
    }
}
