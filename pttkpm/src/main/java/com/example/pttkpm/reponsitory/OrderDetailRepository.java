package com.example.pttkpm.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.OrderDetail;
import java.util.List;
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder(Order order);
}