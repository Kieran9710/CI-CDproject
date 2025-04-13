package org.example.microassign1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderFieldAssignments() {
        Customer customer = new Customer();
        customer.setId(100L);

        Order order = new Order();
        order.setId(1L);
        order.setItem("Tablet");
        order.setAmount(499.99);
        order.setOrderDate(LocalDate.of(2024, 2, 5));
        order.setCustomer(customer);

        assertEquals(1L, order.getId());
        assertEquals("Tablet", order.getItem());
        assertEquals(499.99, order.getAmount());
        assertEquals(LocalDate.of(2024, 2, 5), order.getOrderDate());
        assertEquals(100L, order.getCustomerId());
        assertEquals(customer, order.getCustomer());
    }

    @Test
    void testPrePersistSetsOrderDateIfNull() {
        Order order = new Order();
        order.onCreate();  // simulate JPA lifecycle callback
        assertNotNull(order.getOrderDate());
        assertEquals(LocalDate.now(), order.getOrderDate());
    }
}
