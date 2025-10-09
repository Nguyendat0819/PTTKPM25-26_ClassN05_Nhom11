package com.example.pttkpm.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.OrderDetail;
import com.example.pttkpm.model.Orderstatus;
import com.example.pttkpm.model.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByUserAndStatus(User user, Orderstatus status);

}