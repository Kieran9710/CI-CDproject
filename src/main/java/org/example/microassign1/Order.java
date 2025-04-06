package org.example.microassign1;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;
    private String item;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    @JsonBackReference
    private Customer customer;


    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
    }

    public Long getCustomerId() {
        return this.customer.getId();
    }

    public Long getId() {
        return this.id;
    }
    public LocalDate getOrderDate() {
        return this.orderDate;
    }
    public double getAmount() {
        return this.amount;
    }

    public String getItem() {
        return this.item;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
