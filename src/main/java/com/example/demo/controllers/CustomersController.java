package com.example.demo.controllers;

import com.example.demo.Services.CustomerService;
import com.example.demo.Services.TicketsService;
import com.example.demo.modle.Customers;
import com.example.demo.modle.Tickets;
import com.example.demo.modle.UserWithCustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    @Autowired
    CustomerService customerService;
    @Autowired
    TicketsService ticketsService;
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/ById/{id}")
    public ResponseEntity getCustomerById(@PathVariable Integer id) {
        try {
            Customers customer = customerService.getById(id);
            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/ByUsername/{username}")
    public ResponseEntity<Customers> getCustomerByUsername(@PathVariable String username) {
        try {
            Customers customer = customerService.getCustomerByUsername(username);
            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/getCustomerIdByUsername/{username}")
    public ResponseEntity<Integer> getCustomerIdByUsername(@PathVariable String username) {
        try {
            Customers customer = customerService.getCustomerByUsername(username);
            if (customer != null) {
                return new ResponseEntity<>(customer.getId(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/TicketsByCustomer/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity getTicketsByCustomer(@PathVariable Integer id) {
        try {
            Customers customer = customerService.getById(id);
            if (customer != null) {
                List<Tickets> ticketsList = customerService.getTicketsByCustomer(customer);
                return new ResponseEntity<>(ticketsList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity getAllCustomers() {
        try {
            List<Customers> customers = customerService.getAll();
            return new ResponseEntity(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity addCustomer(@RequestBody UserWithCustomerDTO userWithCustomerDTO) {
        try {
            Customers resultCustomer = customerService.add(userWithCustomerDTO);
            return new ResponseEntity<>(resultCustomer, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/addTicket")
    public ResponseEntity addTicket(@RequestBody Tickets ticket) {
        try {

            Tickets resultTicket = customerService.addTicket(ticket);
            return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/addTicketByUsername/{username}")
    public ResponseEntity addTicket(@RequestBody Tickets ticket,@PathVariable String username) {
        try {

            Tickets resultTicket = customerService.addTicketByUsername(ticket,username);
            return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@PathVariable Integer id, @RequestBody Customers customer) {
        try {
            Customers resultCustomer = customerService.getById(id);
            if (resultCustomer != null) {
                customerService.update(customer, id);
                return new ResponseEntity(resultCustomer, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Customers> deleteCustomer(@PathVariable Integer id) {
        try {
            Customers resultCustomer = customerService.getById(id);
            if (resultCustomer != null) {
                customerService.remove(id);
                return new ResponseEntity<>(resultCustomer, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/removeTicket/{id}")
    public ResponseEntity deleteTicket(@PathVariable Integer id) {
        try {
            Tickets resultTicket = ticketsService.getById(id);
            if (resultTicket != null) {
                customerService.removeTicket(resultTicket);
                return new ResponseEntity<>(resultTicket, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/removeFlight/{flightId}/{username}")
    public ResponseEntity deleteTicketByFlightIdAndUsername(@PathVariable Integer flightId,@PathVariable String username) {
        try {
                customerService.removeTicketByFlightIdAndUsername(flightId,username);
                return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
