package com.example.pttkpm.reponsitory;

import com.example.pttkpm.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
