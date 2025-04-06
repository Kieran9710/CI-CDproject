package org.example.microassign1;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends RepresentationModel<OrderDTO> {
    private LocalDate orderDate;
    @NotBlank(message = "Item cannot be empty")
    private String item;
    private double amount;
    private Long customerId;
    private Long orderId;

    public OrderDTO(Order order, Customer customer) {
        this.orderDate = order.getOrderDate();
        this.item = order.getItem();
        this.amount = order.getAmount();
        this.customerId = customer.getId();
        this.orderId= order.getId();

        add(linkTo(methodOn(OrderController.class).getOrder(orderId)).withSelfRel());
        add(linkTo(methodOn(OrderController.class).getAllOrders(Pageable.unpaged())).withRel("all-orders"));
        add(linkTo(methodOn(OrderController.class).getOrdersByCustomerId(customerId)).withRel("customer-orders"));
        add(linkTo(methodOn(CustomerController.class).getCustomer(customerId)).withRel("customer-details"));
        add(linkTo(methodOn(OrderController.class).deleteOrder(orderId)).withRel("delete-order"));
    }
}