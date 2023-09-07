package com.example.demo.modle;

public enum Roles {
    Customer(1),
    AirlineCompany(2),
    Administrator(3);

    private final int value;

    private Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
