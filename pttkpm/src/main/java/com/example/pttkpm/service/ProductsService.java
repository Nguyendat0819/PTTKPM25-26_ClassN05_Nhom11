package com.example.pttkpm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pttkpm.model.Product;
import com.example.pttkpm.reponsitory.ProductsReponsitory;

@RequestMapping("/api/products") // fetch(`/api/products/${productId}`, {
@Service
public class ProductsService {
    @Autowired
    private ProductsReponsitory productsReponsitory;

    public Product getProductId(Integer productId){
        return productsReponsitory.findByProductId(productId);
    }

    public Product addProducts(Product product) {
        if (productsReponsitory.findByProductNameAndCategoryAndPrice(
                product.getProductName(),
                product.getCategory(),
                product.getPrice()) != null) {
            return null;
        }
        return productsReponsitory.save(product);
    }

    // hàm lấy sản phẩm
    public List<Product> getAllProducts() {
        return productsReponsitory.findAll();
    }

    // cập nhật sản phẩm
    public Product updateProduct(Integer productId, Product newData) {
        return productsReponsitory.findById(productId).map(p -> {
            p.setProductName(newData.getProductName());
            p.setCategory(newData.getCategory());
            p.setPrice(newData.getPrice());
            p.setImageUrl(newData.getImageUrl());
            p.setStatus(newData.getStatus());
            return productsReponsitory.save(p);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có productId = " + productId));
    }

    // Xóa sản phẩm
    public void deleteProduct(Integer productId) {
        if (!productsReponsitory.existsById(productId)) {
            throw new RuntimeException("Không tìm thấy sản phẩm có productId = " + productId);
        }
        productsReponsitory.deleteById(productId);
    }
}
