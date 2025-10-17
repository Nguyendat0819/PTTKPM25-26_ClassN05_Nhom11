package com.example.pttkpm.service;

import com.example.pttkpm.model.Ward;
import com.example.pttkpm.reponsitory.WardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WardService {
    private final WardRepository wardRepository;

    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    public List<Ward> getWardsByDistrictId(Integer districtId) {
        return wardRepository.findByDistrict_DistrictId(districtId);
    }
}
