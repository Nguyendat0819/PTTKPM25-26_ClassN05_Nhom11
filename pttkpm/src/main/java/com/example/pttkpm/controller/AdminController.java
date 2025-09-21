package com.example.pttkpm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @GetMapping("/admin/AdminPage")
    public String AdminPageView() {
        return "admin/AdminPage";
    }

    @GetMapping("/admin/AddProducts")
    public String AddProductsView() {
        return "admin/AddProducts";
    }

    @GetMapping("/admin/DeleteProduct")
    public String DeleteProductView() {
        return "admin/DeleteProduct";
    }

    @GetMapping("/admin/EditProduct")
    public String EditProductView() {
        return "admin/EditProduct";
    }
}