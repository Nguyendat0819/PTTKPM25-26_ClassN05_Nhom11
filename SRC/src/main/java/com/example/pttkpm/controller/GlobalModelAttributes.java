package com.example.pttkpm.controller;

import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.Orderstatus;
import com.example.pttkpm.model.User;
import com.example.pttkpm.reponsitory.OrderDetailRepository;
import com.example.pttkpm.reponsitory.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @ModelAttribute
    public void addCartCountToModel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        int cartCount = 0;
        if (user != null) {
            Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW).orElse(null);
            if (order != null) {
                cartCount = orderDetailRepository.countByOrder(order);
            }
        }
        model.addAttribute("cartCount", cartCount);
    }
}
