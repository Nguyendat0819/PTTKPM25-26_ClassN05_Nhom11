package com.example.pttkpm.reponsitory;

import com.example.pttkpm.model.UserAddress;
import com.example.pttkpm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    // Tìm địa chỉ của 1 user
    UserAddress findByUser(User user);
}
