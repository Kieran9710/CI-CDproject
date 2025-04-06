package org.example.microassign1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderRepo orderRepo;

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepo.findAll(pageable);
    }


    public CustomerDTO getCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customer -> {
                    return new CustomerDTO(customer);

                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + id));
    }

    public CustomerDTO saveCustomer(Customer customer) {
        return new CustomerDTO(customerRepo.save(customer));
    }

    public void deleteCustomer(Long id) {
        customerRepo.findById(id)
                .map(customer -> {
                    return customer;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + id));
        customerRepo.deleteById(id);
    }
}

