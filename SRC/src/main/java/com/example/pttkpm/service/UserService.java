package com.example.pttkpm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.pttkpm.model.User;
import com.example.pttkpm.reponsitory.UserReponsitory;


@Service
public class UserService {
    @Autowired
    private UserReponsitory userReponsitory;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User addUser(User user){
       User ktraEmail = userReponsitory.findByEmail(user.getEmail());
       if (ktraEmail != null) {
            return null;
       }
        // băm mật khẩu
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // lưu user
        return  userReponsitory.save(user);
    }

    public User loginUser(User user,
                          String rawPassword  ){
        User ktraEmail = userReponsitory.findByEmail(user.getEmail());
        if(ktraEmail != null && passwordEncoder.matches( rawPassword ,ktraEmail.getPassword())){
            return ktraEmail;
        }
        return null;
    }

     public User findByname(String username) {
        return userReponsitory.findByName(username);
    }
    

    public List<User> getAllUsers(){
        return userReponsitory.findAll();
    }

}
