package org.example.microassign1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderRepo orderRepo;

    @GetMapping
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderService.getAllOrders(pageable);
        return orders.map(order -> new OrderDTO(order, order.getCustomer()));
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderDTO> getOrdersByCustomerId(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        Customer customer = customerRepo.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + orderDTO.getCustomerId()));
        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setItem(orderDTO.getItem());
        order.setAmount(orderDTO.getAmount());
        order.setCustomer(customer);
        order = orderService.saveOrder(order);

        return new OrderDTO(order, customer);
    }

    @GetMapping("/date-range")
    public Page<OrderDTO> getOrdersByDateRange(@RequestParam LocalDate startDate,
                                               @RequestParam LocalDate endDate,
                                               Pageable pageable) {
        Page<Order> orders = orderRepo.findByOrderDateBetween(startDate, endDate, pageable);
        return orders.map(order -> new OrderDTO(order, order.getCustomer()));
    }

    @DeleteMapping("/{id}")
    public Object deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return null;
    }
}
