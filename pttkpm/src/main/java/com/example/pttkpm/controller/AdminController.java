package com.example.pttkpm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pttkpm.model.Product;

import org.springframework.ui.Model;

@Controller
public class AdminController {
    @GetMapping("/admin/AdminPage")
    public String AdminPageView() {
        return "admin/AdminPage";
    }

    @GetMapping("/admin/addProducts")
    public String addProductsView( Model model) {
        Product product = new Product();
        model.addAttribute("product",product);
        return "admin/addProducts";
    }

    @GetMapping("/admin/editProducts")
    public String editProductsView() {
        return "admin/editProducts";
    }

    @GetMapping("/admin/manageCustomers")
    public String manageCustomerView(){
        return "admin/manageCustomers";
    }


    @GetMapping("/admin/orderStatus")
    public String orderStatusView(){
        return "admin/orderStatus";
    }

    @GetMapping("/admin/profit")
    public String profitView(){
        return "admin/profit";
    }
}