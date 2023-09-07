package com.example.demo.Repository;

import com.example.demo.modle.AirlineCompanies;
import java.util.List;

public interface IAirlineCompaniesRepository {
    AirlineCompanies getById(Integer id);

    List<AirlineCompanies> getAll();

    AirlineCompanies add(AirlineCompanies airlineCompany);

    void update(AirlineCompanies airlineCompany,Integer id);

    List<AirlineCompanies> addAll(List<AirlineCompanies> airlineCompanies);

    void remove(Integer id);
}
