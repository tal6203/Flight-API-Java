package com.example.demo.Repository;

import com.example.demo.modle.Countries;

import java.util.List;

public interface ICountriesRepository {
    Countries getById(Integer id);

    List<Countries> getAll();

    Countries add(Countries country);

    void update(Countries country,Integer id);

    List<Countries> addAll(List<Countries> countries);

    void remove(Integer id);
}
