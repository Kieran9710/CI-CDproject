package org.example.microassign1;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer(1L, "John Doe", "john.doe@example.com", null);
        order = new Order(1L, LocalDate.now(), "Item1", 100.0, customer);
    }

    @Test
    void testGetAllOrders() {
        Pageable pageable = mock(Pageable.class);
        Page<Order> ordersPage = new PageImpl<>(Arrays.asList(order));
        when(orderRepo.findAll(pageable)).thenReturn(ordersPage);

        Page<Order> result = orderService.getAllOrders(pageable);

        assertEquals(1, result.getTotalElements());
        verify(orderRepo, times(1)).findAll(pageable);
    }

    @Test
    void testGetOrdersByCustomerId() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepo.findByCustomer_Id(1L)).thenReturn(Arrays.asList(order));

        List<OrderDTO> orders = orderService.getOrdersByCustomerId(1L);

        assertEquals(1, orders.size());
        verify(customerRepo, atLeastOnce()).findById(1L);
        verify(orderRepo, atLeastOnce()).findByCustomer_Id(1L);
    }

    @Test
    void testSaveOrder() {
        when(orderRepo.save(order)).thenReturn(order);

        Order savedOrder = orderService.saveOrder(order);

        assertNotNull(savedOrder);
        assertEquals("Item1", savedOrder.getItem());
        verify(orderRepo, times(1)).save(order);
    }

    @Test
    void testDeleteOrder() {
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> orderService.deleteOrder(1L));
    }
}
