package com.example.pttkpm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
