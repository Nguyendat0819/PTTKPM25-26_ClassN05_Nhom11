package com.example.pttkpm.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pttkpm.model.Product;
import com.example.pttkpm.service.ProductsService;
// Removed incorrect import for String

@Controller
public class ProductController {
    @Autowired
    private ProductsService productsService;

    // xử lý dữ liệu
    @PostMapping("/addProducts")
    public String productsAddSubmit(
            @ModelAttribute Product product,
            @RequestParam("uploadFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("pttkpm/src/main/resources/static/uploads").toAbsolutePath();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                product.setImageUrl(fileName);
            }
            Product savedProducts = productsService.addProducts(product);
            if (savedProducts != null) {
                redirectAttributes.addFlashAttribute("success", "thêm sản phẩm thành công");
            } else {
                redirectAttributes.addAttribute("error", "sản phẩm tồn tại");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "lỗi khi upload" + e.getMessage());
        }
        return "redirect:/admin/addProducts";
    }

    // hiển thị sản phẩm
    @GetMapping("/user/home")
    public String viewProductHome(Model model) {
        model.addAttribute("productList", productsService.getAllProducts());
        return "user/home";
    }
}
