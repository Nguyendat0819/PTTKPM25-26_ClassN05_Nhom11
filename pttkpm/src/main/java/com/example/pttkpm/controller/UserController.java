package com.example.pttkpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.pttkpm.model.User;
import com.example.pttkpm.service.UserService;
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    // hiển thị trang đăng ký
    @GetMapping("/user/register")
    public String registerView(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "user/register";
    }

    // xử lý dữ liệu trang đăng ký
    @PostMapping("/register")
    public String registerSubmit(
        @ModelAttribute User user
    ) {
        userService.addUser(user);
        return "redirect:/success";
    }

}
