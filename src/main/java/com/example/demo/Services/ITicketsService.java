package com.example.demo.Services;

import com.example.demo.modle.Tickets;

import java.util.List;

public interface ITicketsService {
    Tickets getById(Integer id);

    List<Tickets> getAll();

    Tickets add(Tickets ticket);

    void update(Tickets ticket,Integer id);

    List<Tickets> addAll(List<Tickets> tickets);

    void remove(Integer id);
}
