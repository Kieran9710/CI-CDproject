package org.example.microassign1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return customers.map(customer -> new CustomerDTO(customer));
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public CustomerDTO createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public Object deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return null;
    }
}
