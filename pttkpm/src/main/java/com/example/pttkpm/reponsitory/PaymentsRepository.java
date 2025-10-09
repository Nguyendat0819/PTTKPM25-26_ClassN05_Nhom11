package com.example.pttkpm.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pttkpm.model.Payment;

public interface PaymentsRepository extends JpaRepository<Payment, Integer> {
}
