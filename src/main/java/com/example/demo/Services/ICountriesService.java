package com.example.demo.Services;

import com.example.demo.modle.Countries;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ICountriesService {
    Countries getCountryById(Integer id);

    List<Countries> getAllCountries();

    Countries addCountry(Countries country) throws Exception;

    void updateCountry(Countries country,Integer id) throws JsonProcessingException;

    List<Countries> addAllCountries(List<Countries> countries) throws Exception;

    void removeCountry(Integer id);
}
