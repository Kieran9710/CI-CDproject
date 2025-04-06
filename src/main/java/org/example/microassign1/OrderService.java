package org.example.microassign1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        customerRepo.findById(customerId)
                .map(customer -> {
                    customer.setOrders(orderRepo.findByCustomer_Id(customer.getId()));
                    return customer;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));
        return orderRepo.findByCustomer_Id(customerId)
                .stream()
                .map(order -> new OrderDTO(order, customerRepo.findById(order.getCustomerId()).orElse(null)))
                .toList();
    }

    public OrderDTO getOrderById(Long id) {
        return orderRepo.findById(id)
                .map(order -> new OrderDTO(order, customerRepo.findById(order.getCustomerId()).orElse(null)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + id));
    }

    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepo.findById(id)
                .map(customer -> {
                    return customer;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
        orderRepo.deleteById(id);
    }
}

