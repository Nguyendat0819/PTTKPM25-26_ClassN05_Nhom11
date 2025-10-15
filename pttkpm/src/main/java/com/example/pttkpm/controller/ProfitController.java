package com.example.pttkpm.controller;

import com.example.pttkpm.model.Payment;
import com.example.pttkpm.reponsitory.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ProfitController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/admin/profit")
    public String showProfit(Model model) {
        List<Payment> payments = paymentRepository.findAll();

        // Tổng doanh thu
        BigDecimal totalRevenue = payments.stream()
                .map(Payment::getPaymentAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tổng số đơn hàng
        long totalOrders = payments.stream()
                .filter(p -> p.getOrder() != null)
                .count();

        // Gom doanh thu theo phương thức thanh toán
        Map<String, BigDecimal> revenueByMethod = payments.stream()
                .filter(p -> p.getMethod() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getMethod().name(),
                        Collectors.reducing(BigDecimal.ZERO, Payment::getPaymentAmount, BigDecimal::add)
                ));

        // Chuyển sang JSON cho Thymeleaf
        try {
            ObjectMapper mapper = new ObjectMapper();
            String revenueByMethodJson = mapper.writeValueAsString(revenueByMethod);
            model.addAttribute("revenueByMethodJson", revenueByMethodJson);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("revenueByMethodJson", "{}");
        }

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);

        return "admin/profit";
    }
}
