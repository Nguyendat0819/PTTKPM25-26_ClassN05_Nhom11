package com.example.pttkpm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pttkpm.model.Product;
import com.example.pttkpm.reponsitory.ProductsReponsitory;

@Service
public class ProductsService {
    @Autowired
    private ProductsReponsitory productsReponsitory;

    public Product addProducts(Product product){
        if(productsReponsitory.findByProductNameAndCategoryAndPrice(
            product.getProductName(),
            product.getCategory(),
            product.getPrice()
        ) != null){
            return null;
        }
        return productsReponsitory.save(product);
    }
    
    // hàm lấy sản phẩm 
    public List<Product> getAllProducts(){
        return productsReponsitory.findAll();
    }

}

