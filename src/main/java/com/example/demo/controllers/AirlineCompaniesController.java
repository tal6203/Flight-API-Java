package com.example.demo.controllers;

import com.example.demo.Services.AirlineCompaniesService;
import com.example.demo.Services.FlightsService;
import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/airline-companies")
public class AirlineCompaniesController {
    @Autowired
    AirlineCompaniesService airlineCompaniesService;
    @Autowired
    FlightsService flightsService;
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/{id}")
    public ResponseEntity<AirlineCompanies> getAirlineCompanyById(@PathVariable Integer id) {
        try {
            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineById(id);
            if (airlineCompany != null) {
                return new ResponseEntity<>(airlineCompany, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @PostMapping("/addFlight")
    public ResponseEntity<Flights> addFlight(@RequestBody Flights flight) {
        try {
            airlineCompaniesService.addFlight(flight);
            return new ResponseEntity<>(flight, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/getFlightsByAirline/{id}")
    public ResponseEntity<List<Flights>> getFlightsByAirline(@PathVariable Integer id) {
        try {
            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineById(id);
            if (airlineCompany != null) {
                List<Flights> flightsList = airlineCompaniesService.getFlightsByAirlineId(id);
                return new ResponseEntity<>(flightsList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/getFlightDetailsByUsername/{username}")
    public ResponseEntity<List<FlightDetails>> getFlightDetailsByusernameForAirlineComapny(@PathVariable String username) {
        try {
            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineCompanyByUsername(username);
            if (airlineCompany != null) {
                List<FlightDetails> flightsList = airlineCompaniesService.getFlightsDetailsByUsername(username);
                return new ResponseEntity<>(flightsList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/getAirlineCompanyShowAllDetailsByUsername/{username}")
    public ResponseEntity  getAirlineCompanyShowAllDetailsByUsername(@PathVariable String username) {
        try {

            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineCompanyByUsername(username);
            if (airlineCompany != null) {
                AirlineCompanyDetails AirlineComapnyWithDetails = airlineCompaniesService.getAirlineByUsernameShowAllDetails(username);
                return new ResponseEntity<>(AirlineComapnyWithDetails, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @PutMapping("/UpdateFlightDetailsByUsernameByFlightId/{flightId}/{username}")
    public ResponseEntity UpdateFlightDetailsByUsername(@PathVariable String username ,@PathVariable Integer flightId,@RequestBody FlightDetails flightDetails) {
        try {
            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineCompanyByUsername(username);
            if (airlineCompany != null) {
                System.out.println(flightDetails.toString());
                 airlineCompaniesService.UpdateFlightsDetailsByUsername(username,flightId,flightDetails);
                return new ResponseEntity<>(airlineCompany ,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @PutMapping("/UpdateAirlineWithDetails/{username}/{id}")
    public ResponseEntity UpdateAirlineWithDetails(@PathVariable String username ,@PathVariable Integer id,@RequestBody AirlineCompanyDetails airlineCompanyDetails) {
        try {
            AirlineCompanies airlineCompany = airlineCompaniesService.getAirlineCompanyByUsername(username);
            if (airlineCompany != null) {
                airlineCompaniesService.updateAirlineCompanywithDetails(username,id,airlineCompanyDetails);
                return new ResponseEntity<>(airlineCompany ,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping
    public ResponseEntity getAllAirlineCompanies() {
        try {
            List<AirlineCompanies> airlineCompanies = airlineCompaniesService.getAllAirlines();
            return new ResponseEntity<>(airlineCompanies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity addAirlineCompany(@RequestBody UserWithAirlineCompanyDTO userWithAirlineCompanyDTO) {
        try {
            AirlineCompanies resultAirlineCompany = airlineCompaniesService.addAirlineCompany(userWithAirlineCompanyDTO);
            return new ResponseEntity<>(resultAirlineCompany, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            // Handle the bad request scenario
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @PutMapping("/updateFlight/{flightId}")
    public ResponseEntity<Flights> updateFlight(@PathVariable Integer flightId, @RequestBody Flights flight ) {
        try {
            Flights existingFlight = flightsService.getFlightById(flightId);
            if (existingFlight != null ) {
                    airlineCompaniesService.updateFlight(flight, flightId);
                    return new ResponseEntity<>(existingFlight, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    public ResponseEntity updateAirlineCompany(@PathVariable Integer id, @RequestBody AirlineCompanies airlineCompany) {
        try {
            AirlineCompanies resultAirlineCompany = airlineCompaniesService.getAirlineById(id);
            if (resultAirlineCompany != null) {
                airlineCompaniesService.updateAirlineCompany(airlineCompany, id);
                return new ResponseEntity<>(resultAirlineCompany, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAirlineCompany(@PathVariable Integer id) {
        try {
            AirlineCompanies resultAirlineCompany = airlineCompaniesService.getAirlineById(id);
            if (resultAirlineCompany != null) {
                airlineCompaniesService.removeAirlineCompany(id);
                return new ResponseEntity<>(resultAirlineCompany, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
