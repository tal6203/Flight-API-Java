package com.example.demo.Services;

import com.example.demo.modle.Flights;

import java.util.List;

public interface IFlightsService {
    Flights getFlightById(Integer id);

    List<Flights> getAllFlights();

    Flights addFlight(Flights flight);

    void decreaseRemainingTickets(Integer flightId);
    void incrementRemainingTickets(Integer flightId);
    void updateFlight(Flights flight, Integer id);

    List<Flights> addAllFlights(List<Flights> flights);

    void removeFlight(Integer id);
}
