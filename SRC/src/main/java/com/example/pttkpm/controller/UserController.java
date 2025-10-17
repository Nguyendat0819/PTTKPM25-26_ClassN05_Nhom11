package com.example.pttkpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.pttkpm.model.OrderDetail;
import com.example.pttkpm.model.User;
import com.example.pttkpm.service.UserService;
import com.example.pttkpm.reponsitory.OrderDetailRepository;
import jakarta.servlet.http.HttpSession;
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
        return "redirect:/user/login";
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
    public String loginSubmit(@ModelAttribute("user") User user, HttpSession session, Model model) {
        User loggedInUser = userService.loginUser(user, user.getPassword());
        if (loggedInUser != null) {
            // login thành công → chuyển tới dashboard
            session.setAttribute("loggedInUser", loggedInUser);
            return "redirect:/user/home";
        } else {
            // login thất bại → trả về login với thông báo
            model.addAttribute("error", "Sai email hoặc mật khẩu!");
            return "user/login";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }


    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user, 
                        Model model) {
        model.addAttribute("username", user.getUsername());
        return "profile";
    }


    // @GetMapping("/admin/manageCustomers")
    // public String manageCustomersView(Model model){
    //     List<User> users = userService.getAllUsers();
    //     model.addAttribute("users", users);
    //     return "admin/manageCustomers";
    // }

}
