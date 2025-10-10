package com.example.pttkpm.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pttkpm.model.User;
import java.util.List;


@Repository

public interface UserReponsitory extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByName(String name);


    List<User> findByUserId(Integer userId);
}
