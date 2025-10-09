package com.example.pttkpm.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pttkpm.model.Order;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
}
