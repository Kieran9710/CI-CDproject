package org.example.microassign1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long customerId);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);  // New method



}
