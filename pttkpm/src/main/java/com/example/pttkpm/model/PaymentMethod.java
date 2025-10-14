package com.example.pttkpm.model;

public enum PaymentMethod {
    CASH,
    MOMO,
    CARD;

    public static PaymentMethod fromString(String value) {
        return PaymentMethod.valueOf(value.toUpperCase()); 
    }
}
