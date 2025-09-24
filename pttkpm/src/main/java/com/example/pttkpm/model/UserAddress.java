package com.example.pttkpm.model;

import jakarta.persistence.*;


@Entity
@Table(name = "useraddress")
public class UserAddress {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)

    @Column(name = "userAddress_id")
    private Integer userAddressId;

    @Column(name = "homeAddress",length = 255)
    private String homeAddress;


     // Quan hệ với User (1 user có 1 address)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    
    // quan hệ với province
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    // quan hệ với district
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    //  quan hệ với wards
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    // constructor 

    public UserAddress(){}

    public Integer getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Integer userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    
    
}
