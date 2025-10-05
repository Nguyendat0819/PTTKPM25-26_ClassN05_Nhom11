package com.example.pttkpm.controller;

import com.example.pttkpm.model.*;
import com.example.pttkpm.reponsitory.OrderDetailRepository;
import com.example.pttkpm.reponsitory.OrdersRepository;
import com.example.pttkpm.reponsitory.PaymentsRepository;
import com.example.pttkpm.reponsitory.ProvinceRepository;
import com.example.pttkpm.reponsitory.UserAddressRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class CheckoutController {
    @Autowired
    private UserAddressRepository userAddressRepository;
    
    @Autowired
    private ProvinceRepository provinceRepository;
     @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;
    private final OrderDetailRepository orderDetailRepository;
    public CheckoutController(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    //  Hiển thị trang thanh toán
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

        BigDecimal total = selectedDetails.stream()
                .map(d -> d.getProductPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("selectedDetails", selectedDetails);
        model.addAttribute("total", total);


        //  Lấy địa chỉ giao hàng của user
        UserAddress userAddress = userAddressRepository.findByUser(user);

        // if (userAddress == null) {
        //     model.addAttribute("message", "Bạn chưa có địa chỉ giao hàng, vui lòng cập nhật trước khi thanh toán!");
        //     return "redirect:/user/profile"; // hoặc trang cập nhật địa chỉ
        // }
       
        model.addAttribute("selectedDetails", selectedDetails);
        model.addAttribute("total", total);
        model.addAttribute("userAddress", userAddress);
        model.addAttribute("provinces", provinceRepository.findAll());
        return "user/checkout"; // ✅ view checkout.html trong templates/user/
    }

    //  Xác nhận thanh toán
    @PostMapping("/checkout/doCheckout")
    public String doCheckout(@RequestParam("paymentMethod") PaymentMethod paymentMethod,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        // TODO: xử lý lưu đơn hàng
        redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công!");

        return "redirect:/user/success"; // ✅ redirect thay vì return thẳng view
    }

    // 
    //  Trang success (GET)
    @GetMapping("/success")
    public String viewSuccess() {
        return "user/success"; // ✅ render templates/user/success.html
    }
}
