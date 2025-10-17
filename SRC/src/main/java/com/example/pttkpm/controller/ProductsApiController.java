package com.example.pttkpm.controller;

import com.example.pttkpm.model.Product;
import com.example.pttkpm.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsApiController {

    @Autowired
    private ProductsService productsService;

    // Lấy tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts() {
        return productsService.getAllProducts();
    }

    // Thêm sản phẩm
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productsService.addProducts(product);
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        return productsService.updateProduct(id, product);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Integer id) {
        productsService.deleteProduct(id);
    }
}
