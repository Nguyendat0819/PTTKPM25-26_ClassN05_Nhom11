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
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class CartController {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductsService productService;

    public CartController(OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            ProductsService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }

    // Thêm sản phẩm vào giỏ
    @PostMapping("/cart")
    public String addToCart(@RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        // Kiểm tra order NEW của user
        Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Orderstatus.NEW);
                    newOrder.setTotal_amount(BigDecimal.ZERO); // vì cột NOT NULL
                    return orderRepository.save(newOrder);
                });

        // Lấy sản phẩm
        Product product = productService.getProductId(productId);

        // ✅ Kiểm tra sản phẩm đã có trong giỏ chưa
        List<OrderDetail> details = orderDetailRepository.findByOrderAndProduct(order, product);
        if (!details.isEmpty()) {
            OrderDetail existingDetail = details.get(0); // lấy dòng đầu tiên
            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
            orderDetailRepository.save(existingDetail);

            // Nếu muốn "dọn sạch" các dòng trùng lặp
            if (details.size() > 1) {
                for (int i = 1; i < details.size(); i++) {
                    orderDetailRepository.delete(details.get(i));
                }
            }
        } else {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setProductName(product.getProductName());
            detail.setProductPrice(product.getPrice());
            detail.setQuantity(quantity);
            orderDetailRepository.save(detail);
        }

        // ✅ chuyển tới trang giỏ hàng
        return "redirect:/user/cart";
    }

    // Xem giỏ hàng
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .orElse(null);

        model.addAttribute("order", order);
        return "user/cart"; // -> resources/templates/user/cart.html
    }

    // xử lý trăng giảm số lượng
    @PostMapping("/cart/increase")
    public String increaseQuantity(@RequestParam("detailId") Integer detailId) {
        OrderDetail detail = orderDetailRepository.findById(detailId).orElse(null);
        if (detail != null) {
            detail.setQuantity(detail.getQuantity() + 1);
            orderDetailRepository.save(detail);
        }
        return "redirect:/user/cart";
    }

    @PostMapping("/cart/decrease")
    public String decreaseQuantity(@RequestParam("detailId") Integer detailId) {
        OrderDetail detail = orderDetailRepository.findById(detailId).orElse(null);
        if (detail != null && detail.getQuantity() > 1) {
            detail.setQuantity(detail.getQuantity() - 1);
            orderDetailRepository.save(detail);
        } else if (detail != null && detail.getQuantity() == 1) {
            // Nếu giảm xuống 0 thì xóa luôn sản phẩm khỏi giỏ
            orderDetailRepository.delete(detail);
        }
        return "redirect:/user/cart";
    }

    // xử thanh toán
    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        if (selectedIds == null || selectedIds.isEmpty()) {
            model.addAttribute("message", "Bạn chưa chọn sản phẩm nào để thanh toán!");
            return "redirect:/user/cart";
        }

        // Lấy danh sách OrderDetail đã chọn
        List<OrderDetail> selectedDetails = orderDetailRepository.findAllById(selectedIds);

        // ✅ Tính tổng tiền
        BigDecimal total = selectedDetails.stream()
                .map(d -> d.getProductPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("selectedDetails", selectedDetails);
        model.addAttribute("total", total);
        
        return "user/checkout"; // tạo checkout.html để hiển thị
    }

}
