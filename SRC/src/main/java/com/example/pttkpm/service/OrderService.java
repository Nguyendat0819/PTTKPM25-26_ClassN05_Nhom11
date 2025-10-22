package com.example.pttkpm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.User;
import com.example.pttkpm.reponsitory.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ Lấy tất cả đơn hàng theo user
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // ✅ Lấy đơn hàng theo ID
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

   
}
