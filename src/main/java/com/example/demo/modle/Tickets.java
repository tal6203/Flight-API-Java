package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class Tickets {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected Integer flightId;
    @Getter @Setter
    protected Integer customerId;

    public Tickets(Integer id, Integer flightId, Integer customerId) {
        this.id = id;
        this.flightId = flightId;
        this.customerId = customerId;
    }


    @Override
    public String toString() {
        return "Tickets{" +
                "id=" + id +
                ", flightId=" + flightId +
                ", customerId=" + customerId +
                '}';
    }
}
