package com.example.pttkpm.reponsitory;

import com.example.pttkpm.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByProvince_ProvinceId(Integer provinceId);
}
