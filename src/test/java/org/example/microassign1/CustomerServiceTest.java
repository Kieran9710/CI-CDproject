package org.example.microassign1;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

public class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;
    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer(1L, "John Doe", "john.doe@example.com", null);
    }

    @Test
    void testGetAllCustomers() {
        Pageable pageable = mock(Pageable.class);
        Page<Customer> customersPage = new PageImpl<>(Arrays.asList(customer));
        when(customerRepo.findAll(pageable)).thenReturn(customersPage);

        Page<Customer> result = customerService.getAllCustomers(pageable);

        assertEquals(1, result.getTotalElements());
        verify(customerRepo, times(1)).findAll(pageable);
    }

    @Test
    void testGetCustomerById() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = customerService.getCustomerById(1L);

        assertNotNull(customerDTO);
        assertEquals("John Doe", customerDTO.getName());
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveCustomer() {
        when(customerRepo.save(customer)).thenReturn(customer);

        CustomerDTO customerDTO = customerService.saveCustomer(customer);

        assertNotNull(customerDTO);
        assertEquals("John Doe", customerDTO.getName());
        verify(customerRepo, times(1)).save(customer);
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.deleteCustomer(1L));
    }
}
