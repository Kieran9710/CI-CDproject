package org.example.microassign1;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Data
public class CustomerDTO extends RepresentationModel<CustomerDTO> {
    private Long id;
    private String name;
    private List<OrderDTO> orders;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.orders = (customer.getOrders() != null) ?
                customer.getOrders().stream()
                        .map(order -> new OrderDTO(order, customer))
                        .collect(Collectors.toList())
                : List.of();

        add(linkTo(methodOn(CustomerController.class).getCustomer(id)).withSelfRel());
        add(linkTo(methodOn(CustomerController.class).getAllCustomers(Pageable.unpaged())).withRel("all-customers"));
        add(linkTo(methodOn(OrderController.class).getOrdersByCustomerId(id)).withRel("customer-orders"));
        add(linkTo(methodOn(CustomerController.class).deleteCustomer(id)).withRel("delete-customer"));

    }
}
