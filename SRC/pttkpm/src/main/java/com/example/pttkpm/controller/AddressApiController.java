package com.example.pttkpm.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.pttkpm.model.*;
import com.example.pttkpm.service.*;

@RestController
@RequestMapping("/api")
public class AddressApiController {

    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;

    public AddressApiController(ProvinceService provinceService, DistrictService districtService,
            WardService wardService) {
        this.provinceService = provinceService;
        this.districtService = districtService;
        this.wardService = wardService;
    }

    // ✅ Lấy danh sách Quận/Huyện theo Tỉnh
    @GetMapping("/districts")
    public List<District> getDistricts(@RequestParam Integer provinceId) {
        return districtService.findByProvinceId(provinceId);
    }

    // ✅ Lấy danh sách Phường/Xã theo Quận
    @GetMapping("/wards")
    public List<Ward> getWards(@RequestParam("districtId") Integer districtId) {
        return wardService.getWardsByDistrictId(districtId);
    }
}
