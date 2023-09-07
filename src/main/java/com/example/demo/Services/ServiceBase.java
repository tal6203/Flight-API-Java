package com.example.demo.Services;

import com.example.demo.Repository.AirlineCompaniesRepository;
import com.example.demo.Repository.CountriesRepository;
import com.example.demo.Repository.CustomersRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.modle.AirlineCompanies;
import com.example.demo.modle.Countries;
import com.example.demo.modle.Customers;
import com.example.demo.modle.Flights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
@Service
public abstract class ServiceBase implements IServiceBase{
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private CountriesRepository countriesRepository;


    public List<Flights> getAllFlights() {
        return flightsRepository.getAll();
    }

    public Flights getFlightById(Integer id) {
        return flightsRepository.getById(id);
    }

    public List<Flights> getFlightsByParameters(Integer originCountryId, Integer destinationCountryId, Date date) {
        return flightsRepository.getFlightsByParameters(originCountryId, destinationCountryId, date);
    }

    public List<AirlineCompanies> getAllAirlines() {
        return airlineCompaniesRepository.getAll();
    }

    public AirlineCompanies getAirlineById(Integer id) {
        return airlineCompaniesRepository.getById(id);
    }

    public List<Countries> getAllCountries() {
        return countriesRepository.getAll();
    }

    public Countries getCountryById(Integer id) {
        return countriesRepository.getById(id);
    }
}
