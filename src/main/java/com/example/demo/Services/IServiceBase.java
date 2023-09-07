package com.example.demo.Services;

import com.example.demo.modle.AirlineCompanies;
import com.example.demo.modle.Countries;
import com.example.demo.modle.Customers;
import com.example.demo.modle.Flights;

import java.util.Date;
import java.util.List;

public interface IServiceBase {
    List<Flights> getAllFlights();
    Flights getFlightById(Integer id);
    List<Flights> getFlightsByParameters(Integer originCountryId, Integer destinationCountryId, Date date);
    List<AirlineCompanies> getAllAirlines();
    AirlineCompanies getAirlineById(Integer id);
    List<Countries> getAllCountries();
    Countries getCountryById(Integer id);
}
