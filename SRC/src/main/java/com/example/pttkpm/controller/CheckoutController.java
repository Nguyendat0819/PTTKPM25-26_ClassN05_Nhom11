package com.example.pttkpm.controller;

import com.example.pttkpm.model.*;

import com.example.pttkpm.reponsitory.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class CheckoutController {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserReponsitory userRepository;

    // Hiển thị trang thanh toán
    @PostMapping("/checkout")
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

        List<OrderDetail> selectedDetails = orderDetailRepository.findAllById(selectedIds);

        // Lưu vào session để dùng khi doCheckout
        session.setAttribute("selectedDetails", selectedDetails);

        BigDecimal total = selectedDetails.stream()
                .map(d -> d.getProductPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("selectedDetails", selectedDetails);
        model.addAttribute("total", total);

        // Lấy địa chỉ giao hàng của user
        UserAddress userAddress = userAddressRepository.findByUser(user);
        model.addAttribute("userAddress", userAddress);
        model.addAttribute("provinces", provinceRepository.findAll());

        return "user/checkout";
    }

    // Xác nhận thanh toán
    @PostMapping("/checkout/doCheckout")
    public String doCheckout(@RequestParam("paymentMethod") String paymentMethodStr,
            @RequestParam("homeAddress") String homeAddress,
            @RequestParam("provinceId") Integer provinceId,
            @RequestParam("districtId") Integer districtId,
            @RequestParam("wardId") Integer wardId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<OrderDetail> selectedDetails = (List<OrderDetail>) session.getAttribute("selectedDetails");
        if (selectedDetails == null || selectedDetails.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm để thanh toán!");
            return "redirect:/user/cart";
        }

        // Lấy Province / District / Ward từ repository
        Province province = provinceRepository.findById(provinceId).orElse(null);
        District district = districtRepository.findById(districtId).orElse(null);
        Ward ward = wardRepository.findById(wardId).orElse(null);

        // Lưu hoặc cập nhật địa chỉ
        UserAddress userAddress = userAddressRepository.findByUser(user);
        if (userAddress == null) {
            userAddress = new UserAddress();
            userAddress.setUser(user);
        }
        userAddress.setHomeAddress(homeAddress);
        userAddress.setProvince(province);
        userAddress.setDistrict(district);
        userAddress.setWard(ward);
        userAddressRepository.save(userAddress);
        // Gán địa chỉ cho user
        user.setUserAddress(userAddress);

        // Lưu user để cập nhật cột user.userAddress_id
        userRepository.save(user);

        // Tính tổng
        BigDecimal totalAmount = selectedDetails.stream()
                .map(d -> d.getProductPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lưu Order
        Order order = new Order();
        order.setUser(user);
        order.setTotal_amount(totalAmount);
        order.setStatus(Orderstatus.PAID);
        Order savedOrder = ordersRepository.save(order);

        // Lưu OrderDetail
        for (OrderDetail detail : selectedDetails) {
            OrderDetail newDetail = new OrderDetail();
            newDetail.setOrder(savedOrder);
            newDetail.setProduct(detail.getProduct());
            newDetail.setProductName(detail.getProductName());
            newDetail.setProductPrice(detail.getProductPrice());
            newDetail.setQuantity(detail.getQuantity());
            orderDetailRepository.save(newDetail);
        }

        // Lưu Payment
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setMethod(PaymentMethod.valueOf(paymentMethodStr));
        payment.setPaymentAmount(totalAmount);
        paymentsRepository.save(payment);

        // Sau khi lưu Payment:
        session.setAttribute("successSelectedDetails", selectedDetails);
        session.setAttribute("successTotal", totalAmount);
        session.setAttribute("successPaymentMethod", paymentMethodStr);

        // ------------------ GIỎ HÀNG ------------------
        List<OrderDetail> cartItems = (List<OrderDetail>) session.getAttribute("cartItems");
        List<Integer> selectedProductIds = selectedDetails.stream()
                .map(d -> d.getProduct().getProductId())
                .toList();

        // Nếu chưa có giỏ hàng -> khởi tạo giỏ rỗng
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Xoá các sản phẩm đã thanh toán khỏi giỏ
        cartItems.removeIf(cartItem -> selectedProductIds.contains(cartItem.getProduct().getProductId()));
        session.setAttribute("cartItems", cartItems);

        // Xóa session selectedDetails để làm sạch giỏ hàng
        session.removeAttribute("selectedDetails");

        // Redirect tới trang success (sẽ load dữ liệu từ session)
        return "redirect:/user/success";

    }

    @GetMapping("/success")
    public String viewSuccess(HttpSession session, Model model) {
        List<OrderDetail> selectedDetails = (List<OrderDetail>) session.getAttribute("successSelectedDetails");
        BigDecimal total = (BigDecimal) session.getAttribute("successTotal");
        String paymentMethod = (String) session.getAttribute("successPaymentMethod");

        model.addAttribute("selectedDetails", selectedDetails);
        model.addAttribute("total", total);
        model.addAttribute("paymentMethod", paymentMethod);

        // Xoá để tránh hiển thị lại nếu refresh
        session.removeAttribute("successSelectedDetails");
        session.removeAttribute("successTotal");
        session.removeAttribute("successPaymentMethod");

        return "user/success";
    }

}
