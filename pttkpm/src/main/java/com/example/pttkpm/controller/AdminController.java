package com.example.pttkpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pttkpm.model.Product;
import com.example.pttkpm.model.User;
import com.example.pttkpm.model.Order;
import com.example.pttkpm.reponsitory.OrderRepository;
import com.example.pttkpm.service.ProductsService;
import com.example.pttkpm.service.UserService;

import org.springframework.ui.Model;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;
    
    private final ProductsService productsService;

    AdminController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/admin/AdminPage")
    public String AdminPageView() {
        return "admin/AdminPage";
    }

    @GetMapping("/admin/addProducts")
    public String addProductsView(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "admin/addProducts";
    }

    @GetMapping("/admin/editProducts")
    public String editProductsView(Model model) {
        model.addAttribute("productList", productsService.getAllProducts());
        return "admin/editProducts";
    }

    @GetMapping("/admin/manageCustomers")
    public String manageCustomerView(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manageCustomers";
    }

    @GetMapping("/admin/orderStatus")
    public String orderStatusView( Model model) {
        List<Order> orders = orderRepository.findAll(); // Lấy tất cả đơn hàng
        model.addAttribute("orders", orders); 
        return "admin/orderStatus";
    }

    @GetMapping("/admin/profit")
    public String profitView() {
        return "admin/profit";
    }
}