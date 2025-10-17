package com.example.pttkpm.service;

import com.example.pttkpm.model.Province;
import com.example.pttkpm.reponsitory.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    // ✅ Lấy danh sách tất cả tỉnh/thành
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }

    // ✅ Lấy 1 tỉnh theo ID
    public Optional<Province> getProvinceById(Integer id) {
        return provinceRepository.findById(id);
    }

    // ✅ Thêm hoặc cập nhật tỉnh
    public Province saveProvince(Province province) {
        return provinceRepository.save(province);
    }

    // ✅ Xóa tỉnh
    public void deleteProvince(Integer id) {
        provinceRepository.deleteById(id);
    }
}
