package com.example.demo.Services;

import com.example.demo.modle.AirlineCompanies;
import com.example.demo.modle.Flights;
import com.example.demo.modle.UserWithAirlineCompanyDTO;

import java.util.List;

public interface IAirlineCompaniesService {
    AirlineCompanies addAirlineCompany(UserWithAirlineCompanyDTO userWithAirlineCompanyDTO);
    Flights addFlight(Flights flight);

    void updateAirlineCompany(AirlineCompanies airlineCompany,Integer id);

    List<AirlineCompanies> addAllAirlineCompanies(List<AirlineCompanies> airlineCompanies);

    void removeAirlineCompany(Integer id);
}
