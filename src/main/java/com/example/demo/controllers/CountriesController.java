package com.example.demo.controllers;

import com.example.demo.Services.CountriesService;
import com.example.demo.modle.Countries;
import com.example.demo.modle.UserFaultExcetion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/countries")
public class CountriesController {
    @Autowired
    CountriesService countriesService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity getAllCountries() {
        try {
            List<Countries> countries = countriesService.getAllCountries();
            if (!countries.isEmpty()) {
                return new ResponseEntity<>(countries, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \" There are no countries at all",
                    HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getCountryById(@PathVariable Integer id) {
        try {
            Countries country = countriesService.getCountryById(id);
            if (country != null) {
                return new ResponseEntity<>(country, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addAll")
    public ResponseEntity addCountries(@RequestBody List<Countries> countries) {
        try {
             List<Countries> countriesList = countriesService.addAllCountries(countries);
            return new ResponseEntity<>(countriesList,HttpStatus.CREATED);
        } catch (UserFaultExcetion e) {
            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity addCountry(@RequestBody Countries country) {
        try {
            String json = objectMapper.writeValueAsString(country);
            Countries new_country = objectMapper.readValue(json, Countries.class);
            Countries resultCountry = countriesService.addCountry(new_country);
            return new ResponseEntity<Countries>(resultCountry, HttpStatus.CREATED);
        }
        catch (UserFaultExcetion e) {
            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCountry(@RequestBody Countries country, @PathVariable Integer id) {
        try {
            Countries result = countriesService.getCountryById(id);
            countriesService.updateCountry(country, id);
            if (result != null) {
                return new ResponseEntity<Countries>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"result\": \"created\" }", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCountry(@PathVariable Integer id)  {
        try {
            Countries result = countriesService.getCountryById(id);
            if (result != null) {
                countriesService.removeCountry(id);
                return new ResponseEntity<Countries>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found country with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



