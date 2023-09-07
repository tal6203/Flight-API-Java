package com.example.demo.Repository;
import com.example.demo.modle.Flights;
import java.util.List;

public interface IFlightsRepository {
    Flights getById(Integer id);

    List<Flights> getAll();

    Flights add(Flights flight);

    void update(Flights flight, Integer id);

    void decreaseRemainingTickets(Integer flightId);
    void incrementRemainingTickets(Integer flightId);

    List<Flights> addAll(List<Flights> flights);

    void remove(Integer id);
}
