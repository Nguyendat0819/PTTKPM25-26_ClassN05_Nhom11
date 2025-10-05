package com.example.pttkpm.service;

import com.example.pttkpm.model.District;
import com.example.pttkpm.reponsitory.DistrictRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }
    public List<District> findByProvinceId(Integer provinceId) {
        return districtRepository.findByProvince_ProvinceId(provinceId);
    }
}
