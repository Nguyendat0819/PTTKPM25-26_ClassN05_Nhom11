package com.example.pttkpm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pttkpm.model.OrderDetail;
import com.example.pttkpm.model.Orderstatus;
import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.User;
import com.example.pttkpm.model.Product;
import com.example.pttkpm.service.ProductsService;
import com.example.pttkpm.reponsitory.OrderDetailRepository;
import com.example.pttkpm.reponsitory.OrderRepository;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductsService productService;

    public CartService(OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            ProductsService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }


    public void addToCart(User user, Integer productId, int quantity) {
        // ✅ Lấy order NEW (giỏ hàng hiện tại)
        Order order = orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Orderstatus.NEW);
                    return orderRepository.save(newOrder);
                });

        // ✅ Lấy sản phẩm
        Product product = productService.getProductId(productId);

        // ✅ Kiểm tra xem sản phẩm đã có trong giỏ chưa

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


    }

    public List<OrderDetail> getCartItems(User user) {
        return orderRepository.findByUserAndStatus(user, Orderstatus.NEW)
                .map(orderDetailRepository::findByOrder)
                .orElse(List.of());
    }
}
