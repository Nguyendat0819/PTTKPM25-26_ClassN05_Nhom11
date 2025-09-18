package com.example.pttkpm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/AdminPage")
    public String AdminPageView() {
        return "admin/AdminPage";
    }

}