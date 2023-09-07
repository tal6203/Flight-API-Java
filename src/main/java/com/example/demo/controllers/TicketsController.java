package com.example.demo.controllers;

import com.example.demo.Services.TicketsService;
import com.example.demo.modle.Tickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketsController {
    @Autowired
    private TicketsService ticketsService;
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Tickets> getTicketById(@PathVariable Integer id) {
        try {
            Tickets ticket = ticketsService.getById(id);
            if (ticket != null) {
                return new ResponseEntity<>(ticket, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @GetMapping("/byCustomer/{customerId}")
    public ResponseEntity<List<Tickets>> getTicketsByCustomer(@PathVariable Integer customerId) {
        try {
            List<Tickets> tickets = ticketsService.getTicketsByCustomer(customerId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR') or hasRole('AIRLINE_COMPANY')")
    @GetMapping("/byFlight/{flightId}")
    public ResponseEntity<List<Tickets>> getTicketsByFlight(@PathVariable Integer flightId) {
        try {
            List<Tickets> tickets = ticketsService.getTicketsByFlight(flightId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<Tickets>> getAllTickets() {
        try {
            List<Tickets> tickets = ticketsService.getAll();
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<Tickets> addTicket(@RequestBody Tickets ticket) {
        try {
            Tickets createdTicket = ticketsService.add(ticket);
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @PostMapping("/addAllTickets/")
    public ResponseEntity addAllTickets(@RequestBody List<Tickets> tickets) {
        try {
            List<Tickets> Tickets  = ticketsService.addAll(tickets);
            return new ResponseEntity<>(Tickets, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Tickets> updateTicket(@PathVariable Integer id, @RequestBody Tickets ticket) {
        try {
            Tickets existingTicket = ticketsService.getById(id);
            if (existingTicket != null) {
                ticketsService.update(ticket, id);
                return new ResponseEntity<>(existingTicket, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Tickets> deleteTicket(@PathVariable Integer id) {
        try {
            Tickets deletedTicket = ticketsService.getById(id);
            if (deletedTicket != null) {
                ticketsService.remove(id);
                return new ResponseEntity<>(deletedTicket, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
