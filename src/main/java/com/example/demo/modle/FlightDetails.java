package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlightDetails {
    private int id;
    private String airlineCompany;
    private String originCountry;
    private String destinationCountry;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private int remainingTickets;

    public FlightDetails(int id, String airlineCompany, String originCountry, String destinationCountry, LocalDateTime departureTime, LocalDateTime landingTime, int remainingTickets) {
        this.id = id;
        this.airlineCompany = airlineCompany;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.remainingTickets = remainingTickets;
    }


    @Override
    public String toString() {
        return "FlightDetails{" +
                "id=" + id +
                ", airlineCompany='" + airlineCompany + '\'' +
                ", originCountry='" + originCountry + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", departureTime=" + departureTime +
                ", landingTime=" + landingTime +
                ", remainingTickets=" + remainingTickets +
                '}';
    }
}
