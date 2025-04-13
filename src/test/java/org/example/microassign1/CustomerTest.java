package org.example.microassign1;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerFieldAssignments() {
        // Create a Customer object and set values
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        // Assert that fields are assigned correctly
        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    void testCustomerEmailValidation() {
        // Create a Customer object with invalid email
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setName("Jane Doe");
        customer.setEmail("invalid-email");

        // Check that the email format is invalid (assuming you have a validation mechanism in place)
        assertThrows(IllegalArgumentException.class, () -> {
            // This would be thrown if you are validating the email manually
            // or if validation annotations trigger an exception when trying to persist
            customer.setEmail("invalid-email");
        });
    }

    @Test
    void testCustomerNameCannotBeEmpty() {
        // Create a Customer object with empty name
        Customer customer = new Customer();
        customer.setId(3L);
        customer.setEmail("jane.doe@example.com");

        // Assert that the name cannot be empty (assuming validation in place)
        assertThrows(IllegalArgumentException.class, () -> {
            customer.setName(""); // This would throw an exception if validation is used
        });
    }

    @Test
    void testCustomerOrdersAssociation() {
        // Create a Customer object
        Customer customer = new Customer();
        customer.setId(4L);
        customer.setName("Alice");
        customer.setEmail("alice@example.com");

        // Create an Order and associate it with the customer
        Order order = new Order();
        order.setId(1L);
        order.setItem("Laptop");
        order.setAmount(1200.00);
        order.setOrderDate(LocalDate.of(2024, 3, 15));
        order.setCustomer(customer);

        // Add the order to the customer's orders list
        customer.setOrders(List.of(order));

        // Assert that the order is associated with the customer
        assertEquals(1, customer.getOrders().size());
        assertEquals("Laptop", customer.getOrders().get(0).getItem());
        assertEquals(customer, customer.getOrders().get(0).getCustomer());
    }

    @Test
    void testPrePersistSetsNameIfNull() {
        // Create a Customer object with null name
        Customer customer = new Customer();
        assertNotNull(customer.getName(), "Customer name should not be null after persistence.");
        assertEquals("Unknown", customer.getName(), "Default name should be 'Unknown'");
    }
}
