package com.example.pttkpm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pttkpm.model.OrderDetail;
import com.example.pttkpm.model.Orderstatus;
import com.example.pttkpm.model.User;
import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.Product;
import com.example.pttkpm.reponsitory.OrderRepository;
import com.example.pttkpm.reponsitory.OrderDetailRepository;
import com.example.pttkpm.service.UserService;
import com.example.pttkpm.service.ProductsService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductsService productService;
    private final UserService userService;

    public CartController(OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            ProductsService productService,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.userService = userService;
    }

    // ✅ Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity,
            Principal principal) {

        if (principal == null) {
            return "redirect:/user/login";
        }

        // Lấy user từ Principal (theo name)
        User user = userService.findByname(principal.getName());

        // Kiểm tra order PENDING/NEW
        Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Orderstatus.NEW);
                    return orderRepository.save(newOrder);
                });

        // Lấy sản phẩm
        Product product = productService.getProductId(productId);

        // Tạo order detail
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setProductName(product.getProductName());
        orderDetail.setProductPrice(product.getPrice());
        orderDetail.setQuantity(quantity);

        orderDetailRepository.save(orderDetail);

        return "redirect:/user/cart";
    }

    // ✅ Xem giỏ hàng
    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        User user = userService.findByname(principal.getName());
        Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .orElse(null);

        model.addAttribute("order", order);
        return "cart"; // cart.html
    }

    
}
