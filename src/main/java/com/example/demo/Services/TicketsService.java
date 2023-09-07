package com.example.demo.Services;

import com.example.demo.Repository.*;
import com.example.demo.modle.FlightFullyBookedException;
import com.example.demo.modle.Tickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TicketsService implements ITicketsService{
    @Autowired
    private TicketsRepository ticketsRepository;
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;

    @Value("${min_RemainingTickets}")
    private Integer min_RemainingTickets;

    public List<Tickets> getTicketsByCustomer(Integer customerId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(customersRepository.getById(customerId).getUser_id()).getUsername();
        if (currentUsername.equals(username)) {
            return ticketsRepository.getTicketsByCustomer(customerId);
        }
        throw new RuntimeException("Information about a tickets is given only to the user to whom he is connected");
    }

    public List<Tickets> getTicketsByFlight(Integer flightId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(airlineCompaniesRepository.getById(flightsRepository.getById(flightId).getAirlineCompanyId()).getUserId()).getUsername();
        if (currentUsername.equals(username)) {
            return ticketsRepository.getTicketsByFlight(flightId);
        }
        throw new RuntimeException("Information about a ticket is given only to the user to whom he is connected");
    }

    @Override
    public Tickets getById(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(customersRepository.getById(ticketsRepository.getById(id).getCustomerId()).getUser_id()).getUsername();
        if (currentUsername.equals(username)) {
            return ticketsRepository.getById(id);
        }
        throw new RuntimeException("Information about a ticket is given only to the user to whom he is connected");
    }

    @Override
    public List<Tickets> getAll() {
        return ticketsRepository.getAll();
    }

    @Override
    public Tickets add(Tickets ticket) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(customersRepository.getById(ticket.getCustomerId()).getUser_id()).getUsername();
        if (currentUsername.equals(username)) {
            if (flightsRepository.getById(ticket.getFlightId()).getRemainingTickets() <= min_RemainingTickets) {
                throw new FlightFullyBookedException("Flight with ID " + ticket.getFlightId() + " is fully booked.");
            }
            Tickets createdTicket = ticketsRepository.add(ticket);

            // Decrease remaining tickets only if the ticket was added successfully
            flightsRepository.decreaseRemainingTickets(createdTicket.getFlightId());
            return createdTicket;
        }
        throw new RuntimeException("Buying a ticket will only be granted from that logged in user");
    }

    @Override
    public void update(Tickets ticket, Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(customersRepository.getById(ticket.getCustomerId()).getUser_id()).getUsername();
        if (currentUsername.equals(username)) {
            ticketsRepository.update(ticket, id);
        }
        throw new RuntimeException("A flight ticket update will only be given by that logged in user");
    }

    @Override
    public List<Tickets> addAll(List<Tickets> tickets) {
        for (Tickets ticket: tickets) {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String username = usersRepository.getById(customersRepository.getById(ticket.getCustomerId()).getUser_id()).getUsername();
            if (currentUsername.equals(username)) {
                if (flightsRepository.getById(ticket.getFlightId()).getRemainingTickets() <= min_RemainingTickets) {
                    throw new FlightFullyBookedException("Flight with ID " + ticket.getFlightId() + " is fully booked.");
                } else {
                    Tickets createdTicket = ticketsRepository.add(ticket);
                    flightsRepository.decreaseRemainingTickets(createdTicket.getFlightId());
                }
            }
            throw new RuntimeException("Buying a ticket will only be granted from that logged in user");
        }
       return ticketsRepository.addAll(tickets);
    }

    @Override
    public void remove(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = usersRepository.getById(customersRepository.getById(ticketsRepository.getById(id).getCustomerId()).getUser_id()).getUsername();
        if (currentUsername.equals(username)) {
            Tickets exists_ticket = ticketsRepository.getById(id);
            if (exists_ticket != null) {
                ticketsRepository.remove(id);
                flightsRepository.incrementRemainingTickets(exists_ticket.getFlightId());
            } else {
                throw new NoSuchElementException("Ticket does not exist");
            }
        }
        throw new RuntimeException("Flight ticket cancellation will only be given by that logged in user");
    }
}
