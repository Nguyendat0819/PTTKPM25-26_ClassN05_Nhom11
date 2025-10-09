package com.example.pttkpm.reponsitory;

import com.example.pttkpm.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByDistrict_DistrictId(Integer districtId);
}
