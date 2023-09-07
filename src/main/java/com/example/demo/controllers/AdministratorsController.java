package com.example.demo.controllers;

import com.example.demo.Services.AdministratorService;
import com.example.demo.Services.AirlineCompaniesService;
import com.example.demo.Services.CustomerService;
import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/administrators")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdministratorsController {

    @Autowired
    AdministratorService administratorService;

    @Autowired
    CustomerService customerService;
    @Autowired
    AirlineCompaniesService airlineCompaniesService;

    @GetMapping("/{id}")
    public ResponseEntity getAdministratorById(@PathVariable Integer id) {
        try {
            Administrators administrator = administratorService.getById(id);
            if (administrator != null) {
                return new ResponseEntity<>(administrator, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getAllAdministrators() {
        try {
            List<Administrators> administrators = administratorService.getAll();
            return new ResponseEntity<>(administrators, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllAirlineCompanies")
    public ResponseEntity getAllAirlineCompanies(){
        try {
            List<AirlineCompanyDetails> airlineCompaniesList = administratorService.getAllAirlineCompaniesWithDetails();
            return new ResponseEntity<>(airlineCompaniesList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity getAllCustomers() {
        try {
            List<Customers> customers = administratorService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addCustomer")
    public ResponseEntity addCustomer(@RequestBody UserWithCustomerDTO userWithCustomerDTO) {
        try {
            Customers resultCustomer = administratorService.addCustomer(userWithCustomerDTO);
            return new ResponseEntity<>(resultCustomer, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addAirlineCompany")
    public ResponseEntity addAirlineCompany(@RequestBody UserWithAirlineCompanyDTO userWithAirlineCompanyDTO) {
        try {
            AirlineCompanies resultAirlineCompany = administratorService.addAirlineCompany(userWithAirlineCompanyDTO);
            return new ResponseEntity<>(resultAirlineCompany, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity addAdministrator(@RequestBody UserWithAdministratorDTO userWithAdministratorDTO) {
        try {
            Administrators resultAdministrator = administratorService.addAdministrator(userWithAdministratorDTO);
            return new ResponseEntity<>(resultAdministrator, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAdministrator(@PathVariable Integer id, @RequestBody Administrators administrator) {
        try {
            Administrators resultAdministrator = administratorService.getById(id);
            if (resultAdministrator != null) {
                administratorService.update(administrator,id);
                return new ResponseEntity<>(resultAdministrator, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAdministrator(@PathVariable Integer id) {
        try {
            Administrators resultAdministrator = administratorService.getById(id);
            if (resultAdministrator != null) {
                administratorService.removeAdministrator(id);
                return new ResponseEntity<>(resultAdministrator, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeCustomer/{id}")
    public ResponseEntity deleteCustomer(@PathVariable Integer id) {
        try {
            Customers resultCustomer = customerService.getById(id);
            if (resultCustomer != null) {
                administratorService.removeCustomer(resultCustomer);
                return new ResponseEntity<>(resultCustomer, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeAirlineCompany/{id}")
    public ResponseEntity deleteAirlineCompany(@PathVariable Integer id) {
        try {
            AirlineCompanies resultAirlineCompany = airlineCompaniesService.getAirlineById(id);
            if (resultAirlineCompany != null) {
                administratorService.removeAirlineCompany(resultAirlineCompany);
                return new ResponseEntity<>(resultAirlineCompany, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
