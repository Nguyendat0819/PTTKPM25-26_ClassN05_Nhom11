package com.example.pttkpm.controller;

import com.example.pttkpm.model.Order;
import com.example.pttkpm.model.User;
import com.example.pttkpm.model.UserAddress;
import com.example.pttkpm.service.CartService;
import com.example.pttkpm.service.OrderService;
import com.example.pttkpm.reponsitory.UserAddressRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class HistoryBuyController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @GetMapping("/user/buy")
    public String ViewBuy(HttpSession session, Model model){
        //  Lấy user đang đăng nhập
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        //  Lấy danh sách đơn hàng theo user
        List<Order> orders = orderService.getOrdersByUser(loggedInUser);
        model.addAttribute("orders", orders);

        // ✅ Lấy địa chỉ user từ bảng useraddress
        UserAddress userAddress = userAddressRepository.findByUser(loggedInUser);

        // ✅ Ghép địa chỉ đầy đủ
        String fullAddress = "Chưa có địa chỉ";
        if (userAddress != null) {
            String provinceName = (userAddress.getProvince() != null) ? userAddress.getProvince().getName() : "";
            String districtName = (userAddress.getDistrict() != null) ? userAddress.getDistrict().getName() : "";
            String wardName = (userAddress.getWard() != null) ? userAddress.getWard().getName() : "";

            fullAddress = String.join(", ",
                    userAddress.getHomeAddress(),
                    wardName,
                    districtName,
                    provinceName);
        }

        // ✅ Gửi sang view
        model.addAttribute("userPhone", loggedInUser.getPhone());
        model.addAttribute("userAddress", fullAddress);

        return "user/buy";
    }
}
