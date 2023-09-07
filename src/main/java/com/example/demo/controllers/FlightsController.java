package com.example.demo.controllers;

import com.example.demo.Services.CustomerService;
import com.example.demo.Services.FlightsService;
import com.example.demo.modle.AccessDeniedException;
import com.example.demo.modle.FlightDetails;
import com.example.demo.modle.Flights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/flights")
public class FlightsController {

    @Autowired
     FlightsService flightsService;
    @Autowired
    CustomerService customerService;
    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/{id}")
    public ResponseEntity<Flights> getFlightById(@PathVariable Integer id) {
        Flights flight = flightsService.getFlightById(id);
        if (flight != null) {
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Flights>> getAllFlights() {
        List<Flights> flights = flightsService.getAllFlights();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('AIRLINE_COMPANY') or hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<Flights> addFlight(@RequestBody Flights flight) {
        try {
            flightsService.addFlight(flight);
            return new ResponseEntity<>(flight, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }


    @PreAuthorize("hasRole('AIRLINE_COMPANY') or hasRole('ADMINISTRATOR')")
    @PostMapping("/addFlightWithDetails")
    public ResponseEntity addFlightWithDetails(@RequestBody FlightDetails flightWithDetails) {
        try {
           Flights flight = flightsService.addFlightWithDetails(flightWithDetails);
            return new ResponseEntity<>(flight, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY') or hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity updateFlight(@PathVariable Integer id, @RequestBody Flights flight) {
        try {
            Flights  existingFlight = flightsService.getFlightById(id);
            if (existingFlight != null)
            flightsService.updateFlight(flight, id);
            return new ResponseEntity<>(flight, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("error: "+ e.getMessage(),HttpStatus.NOT_FOUND); // 400 Bad Request
        } catch (Exception e) {
            return new ResponseEntity<>("error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }



    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Flights> deleteFlight(@PathVariable Integer id) {
        Flights existingFlight = flightsService.getFlightById(id);
        if (existingFlight != null) {
            flightsService.removeFlight(id);
            return new ResponseEntity<>(existingFlight, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @GetMapping("/byCustomer/{customerId}")
    public ResponseEntity getFlightsByCustomer(@PathVariable Integer customerId) throws AccessDeniedException {
        List<Flights> flights = flightsService.getFlightsByCustomer(customerService.getById(customerId));
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @GetMapping("/byUsername/{username}")
    public ResponseEntity getFlightsByUsername(@PathVariable String username)  {
        try {
            List<FlightDetails> flights = flightsService.getFlightsByUsername(username);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/byOriginCountry/{countryId}")
    public ResponseEntity<List<Flights>> getFlightsByOriginCountry(@PathVariable Integer countryId) {
        List<Flights> flights = flightsService.getFlightsByOriginCountryId(countryId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/byDestinationCountry/{countryId}")
    public ResponseEntity<List<Flights>> getFlightsByDestinationCountry(@PathVariable Integer countryId) {
        List<Flights> flights = flightsService.getFlightsByDestinationCountryId(countryId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/byDepartureDate/{date}")
    public ResponseEntity<List<Flights>> getFlightsByDepartureDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate) {

        List<Flights> flights = flightsService.getFlightsByDepartureDate(departureDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/byLandingDate/{date}")
    public ResponseEntity<List<Flights>> getFlightsByLandingDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date landingDate) {
        List<Flights> flights = flightsService.getFlightsByLandingDate(landingDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/byParameters/{originCountryId}/{destinationCountryId}/{date}")
    public ResponseEntity<List<Flights>> getFlightsByParameters(@PathVariable Integer originCountryId,
                                                                @PathVariable Integer destinationCountryId,
                                                                @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<Flights> flights = flightsService.getFlightsByParameters(originCountryId, destinationCountryId, date);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AIRLINE_COMPANY')")
    @GetMapping("/byAirline/{airlineId}")
    public ResponseEntity<List<Flights>> getFlightsByAirlineId(@PathVariable Integer airlineId) {
        List<Flights> flights = flightsService.getFlightsByAirlineId(airlineId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/arrivalFlights/{countryId}")
    public ResponseEntity<List<Flights>> getArrivalFlights(@PathVariable Integer countryId) {
        List<Flights> flights = flightsService.getArrivalFlights(countryId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/departureFlights/{countryId}")
    public ResponseEntity<List<Flights>> getDepartureFlights(@PathVariable Integer countryId) {
        List<Flights> flights = flightsService.getDepartureFlights(countryId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}
