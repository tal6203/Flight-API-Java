package com.example.demo.modle;

public class FlightFullyBookedException extends RuntimeException {
    public FlightFullyBookedException(String message) {
        super(message);
    }
}
