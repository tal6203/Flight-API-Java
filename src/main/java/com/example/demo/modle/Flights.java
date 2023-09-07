package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Flights {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected Integer airlineCompanyId;
    @Getter @Setter
    protected Integer originCountryId;
    @Getter @Setter
    protected Integer destinationCountryId;
    @Getter @Setter
    protected LocalDateTime departureTime;
    @Getter @Setter
    protected LocalDateTime landingTime;
    @Getter @Setter
    protected Integer remainingTickets;


    public Flights(Integer id, Integer airlineCompanyId, Integer originCountryId, Integer destinationCountryId,
                   LocalDateTime departureTime, LocalDateTime landingTime, Integer remainingTickets) {
        this.id = id;
        this.airlineCompanyId = airlineCompanyId;
        this.originCountryId = originCountryId;
        this.destinationCountryId = destinationCountryId;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.remainingTickets = remainingTickets;
    }


    @Override
    public String toString() {
        return "Flights{" +
                "id=" + id +
                ", airlineCompanyId=" + airlineCompanyId +
                ", originCountryId=" + originCountryId +
                ", destinationCountryId=" + destinationCountryId +
                ", departureTime=" + departureTime +
                ", landingTime=" + landingTime +
                ", remainingTickets=" + remainingTickets +
                '}';
    }
}
