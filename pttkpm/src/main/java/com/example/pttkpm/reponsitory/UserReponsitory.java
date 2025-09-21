package com.example.pttkpm.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pttkpm.model.User;

@Repository

public interface UserReponsitory extends JpaRepository<User, Integer> {
    
}
