package com.example.pttkpm.model;

public enum Orderstatus {
    NEW,
    PAID,
    CANCELLED;

    public static Orderstatus fromString(String value) {
        return Orderstatus.valueOf(value.toUpperCase()); 
    }
}
