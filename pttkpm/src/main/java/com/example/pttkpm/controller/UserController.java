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

    // hiển thị Login
    @GetMapping("/user/login")
    public String loginView(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "user/login";
    }

    // xử lý login
    @PostMapping("/user/login")
    public String loginSubmit(@ModelAttribute("user") User user, Model model) {
        User loggedInUser = userService.loginUser(user, user.getPassword());
        if (loggedInUser != null) {
            // login thành công → chuyển tới dashboard
            return "redirect:/user/home";
        } else {
            // login thất bại → trả về login với thông báo
            model.addAttribute("error", "Sai email hoặc mật khẩu!");
            return "user/login";
        }
    }

    // //Hiển thị Home
    // @GetMapping("/user/home")
    // public String homeView(){
    //     return "/user/home";
    // }
}
