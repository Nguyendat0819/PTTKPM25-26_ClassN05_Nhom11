package com.example.pttkpm.reponsitory;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pttkpm.model.Product;

@Repository
public interface ProductsReponsitory extends JpaRepository<Product,Integer> {
    Product findByProductNameAndCategoryAndPrice(String productName, String category, BigDecimal price );
    
    Product findByProductId(Integer productId);
}
