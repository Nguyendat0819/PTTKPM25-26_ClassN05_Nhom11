package com.example.pttkpm.model;

import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "district")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "name", length = 64,nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // lazy loading khi  nhiều quận huyện gắn với 1 tỉnh thành
    @JoinColumn(name = "province_id") // dùng cho khóa ngoại 
    private Province province;

    // 1 quận huyện có nhiều xã
    @OneToMany(mappedBy ="district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ward> wards;


    public District(){}


    public Integer getDistrictId() {
        return districtId;
    }


    public String getName() {
        return name;
    }


    public Province getProvince() {
        return province;
    }


    public List<Ward> getWards() {
        return wards;
    }


    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setProvince(Province province) {
        this.province = province;
    }


    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }

    // getter and setter
    



}
