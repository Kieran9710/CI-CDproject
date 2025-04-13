package org.example.microassign1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private Customer sampleCustomer;
    private CustomerDTO sampleCustomerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCustomer = new Customer(1L, "John Doe", "john@example.com", null);
        sampleCustomerDTO = new CustomerDTO(sampleCustomer);
    }

    @Test
    void testGetCustomerById() {
        when(customerService.getCustomerById(1L)).thenReturn(sampleCustomerDTO);

        CustomerDTO result = customerController.getCustomer(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerService).getCustomerById(1L);
    }

    @Test
    void testCreateCustomer() {
        when(customerService.saveCustomer(sampleCustomer)).thenReturn(sampleCustomerDTO);

        CustomerDTO result = customerController.createCustomer(sampleCustomer);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerService).saveCustomer(sampleCustomer);
    }

    @Test
    void testDeleteCustomer() {
        doNothing().when(customerService).deleteCustomer(1L);

        customerController.deleteCustomer(1L);

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void testGetAllCustomers() {
        // Prepare Pageable mock and Page<Customer> mock
        Pageable pageable = mock(Pageable.class);
        Page<Customer> customerPage = new PageImpl<>(List.of(sampleCustomer));

        // When the service method is called, return a Page<Customer>
        when(customerService.getAllCustomers(pageable)).thenReturn(customerPage);

        // Call the controller method, which will convert Page<Customer> to Page<CustomerDTO>
        Page<CustomerDTO> result = customerController.getAllCustomers(pageable);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());  // Assuming there is only one customer
        assertEquals("John Doe", result.getContent().get(0).getName());
        verify(customerService).getAllCustomers(pageable);
    }
}
