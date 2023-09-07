package com.example.demo.Services;

import com.example.demo.Repository.CacheRepository;
import com.example.demo.Repository.CountriesRepository;
import com.example.demo.modle.Countries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Service
public class CountriesService implements ICountriesService{

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private CacheRepository cacheRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Value("${cache_on}")
    private Boolean cache_on;

    private static final String COUNTRIES_CACHE_KEY = "countries_cache";

    @Override
    public Countries getCountryById(Integer id) {
        return countriesRepository.getById(id);
    }

    @Override
    public List<Countries> getAllCountries() {
        if (cache_on && cacheRepository.isKeyExist(COUNTRIES_CACHE_KEY)) {
            String countriesList = cacheRepository.getCacheEntity(COUNTRIES_CACHE_KEY);
            if (!countriesList.isEmpty()) {
                try {
                    List<Countries> countries = Arrays.asList(objectMapper.readValue(countriesList, Countries[].class));
                    System.out.println("Brings the countries from the cache");
                    return countries;
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

        List<Countries> countries = countriesRepository.getAll();

        if (cache_on && !countries.isEmpty()) {
            try {
                String countriesList = objectMapper.writeValueAsString(countries);
                cacheRepository.createCacheEntity(COUNTRIES_CACHE_KEY, countriesList);
            } catch (JsonProcessingException e) {
                System.out.println(e);
            }
        }

        return countries;
    }


    @Override
    public Countries addCountry(Countries country) throws Exception{
            countriesRepository.add(country);
        return country;
    }

    @Override
    public void updateCountry(Countries country, Integer id) throws JsonProcessingException {
        countriesRepository.update(country, id);
        if (cache_on && cacheRepository.isKeyExist(COUNTRIES_CACHE_KEY)) {
            String countryJson;
            try {
                countryJson = objectMapper.writeValueAsString(country);
            } catch (JsonProcessingException e) {
                // Handle JSON serialization error appropriately (e.g., log the error)
                throw new RuntimeException("Failed to serialize country to JSON: " + e.getMessage());
            }
            String specificCacheKey = COUNTRIES_CACHE_KEY + String.valueOf(id);
            if (cacheRepository.isKeyExist(specificCacheKey)) {
                cacheRepository.updateCacheEntity(specificCacheKey, countryJson);
            }
        }
    }

    @Override
    public List<Countries> addAllCountries(List<Countries> countries) throws Exception {
           return countriesRepository.addAll(countries);
    }

    @Override
    public void removeCountry(Integer id) {
        countriesRepository.remove(id);
    }
}
