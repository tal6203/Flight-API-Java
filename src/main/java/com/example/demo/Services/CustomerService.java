package com.example.demo.Services;

import com.example.demo.Repository.CustomersRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.TicketsRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService extends ServiceBase implements ICustomerService{
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private TicketsRepository ticketsRepository;

    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Value("${min_RemainingTickets}")
    private Integer min_RemainingTickets;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customers getCustomerByUsername(String username) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String usernameByCustomer = usersRepository.getUserByUsername(username).getUsername();
        if (currentUsername.equals(usernameByCustomer)){
        return customersRepository.getCustomerByUsername(username);
        }
            throw new AccessDeniedException("Only that logged in customer can access their personal details");
    }

    public Tickets addTicketByUsername(Tickets ticket , String username) throws TicketOrderNotAllowedException {
        Flights flight = flightsRepository.getById(ticket.getFlightId());
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String check_username = usersRepository.getByUsername(username).getUsername();
        if (flight.getRemainingTickets() <= min_RemainingTickets) {
            throw new IllegalStateException("Flight is fully booked");
        }
        if (!currentUsername.equals(check_username)){
            throw new TicketOrderNotAllowedException(" Only that customer can order a ticket for himself");
        }
        Integer customerId = customersRepository.getCustomerByUsername(username).getId();
        ticket.setCustomerId(customerId);
        flightsRepository.decreaseRemainingTickets(ticket.getFlightId());
        return ticketsRepository.add(ticket);
    }


    @Override
    public Tickets addTicket(Tickets ticket) throws TicketOrderNotAllowedException {
        Flights flight = flightsRepository.getById(ticket.getFlightId());
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String usernameByCustomer = usersRepository.getById(customersRepository.getById(ticket.getCustomerId()).getUser_id()).getUsername();
        if (flight.getRemainingTickets() <= min_RemainingTickets) {
            throw new IllegalStateException("Flight is fully booked");
        }
        if (!currentUsername.equals(usernameByCustomer)){
            throw new TicketOrderNotAllowedException(" Only that customer can order a ticket for himself");
        }
        flightsRepository.decreaseRemainingTickets(ticket.getFlightId());
        return ticketsRepository.add(ticket);
    }


    @Override
    public void removeTicket(Tickets ticket) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String usernameByCustomer = usersRepository.getById(customersRepository.getById(ticket.getCustomerId()).getUser_id()).getUsername();
            if (currentUsername.equals(usernameByCustomer)) {
                flightsRepository.incrementRemainingTickets(ticket.getFlightId());
                ticketsRepository.remove(ticket.getId());
            }
            else {
                throw new RuntimeException("A customer cannot cancel a ticket that belongs to another customer");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to remove ticket: " + e.getMessage());
        }
    }

    public void removeTicketByFlightIdAndUsername(Integer flightId,String username) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String usernameByCustomer = username;
            if (currentUsername.equals(usernameByCustomer)) {
                flightsRepository.incrementRemainingTickets(flightId);
                Integer customerId = customersRepository.getCustomerByUsername(username).getId();
                ticketsRepository.remove(ticketsRepository.getTicktByFlightIdAndCustomerId(flightId,customerId).getId());
            }
            else {
                throw new RuntimeException("A customer cannot cancel a ticket that belongs to another customer");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to remove ticket: " + e.getMessage());
        }
    }

    @Override
    public List<Tickets> getTicketsByCustomer(Customers customer) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String usernameByCustomer = usersRepository.getById(customer.getUser_id()).getUsername();
            if (currentUsername.equals(usernameByCustomer)) {
                return ticketsRepository.getTicketsByCustomer(customer.getId());
            }
            throw new RuntimeException("Failed to get tickets by customer: a customer can only see his own tickets");
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get tickets by customer: " + e.getMessage());
        }
    }

    @Override
    public Customers getById(Integer id) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String usernameByCustomer = usersRepository.getById(customersRepository.getById(id).getUser_id()).getUsername();
        if (currentUsername.equals(usernameByCustomer)){
            return customersRepository.getById(id);
        }
        throw new AccessDeniedException("Only that logged in customer can access their personal details");
    }


    @Override
    public List<Customers> getAll() {
        return customersRepository.getAll();
    }

    @Override
    public Customers add(UserWithCustomerDTO userWithCustomerDTO) {
        userWithCustomerDTO.getUser().setPassword(passwordEncoder.encode(userWithCustomerDTO.getUser().getPassword()));
        Users user = usersRepository.add(userWithCustomerDTO.getUser());
        Customers customer = userWithCustomerDTO.getCustomer();
        customer.setUser_id(user.getId());
       return customersRepository.add(customer);
    }

    @Override
    public void update(Customers customer, Integer id) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String usernameByCustomer = usersRepository.getById(customersRepository.getById(id).getUser_id()).getUsername();
            if (currentUsername.equals(usernameByCustomer)) {
                customersRepository.update(customer, id);
            }
            else {
                throw new RuntimeException("Only a customer can update themselves");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to update customer: " + e.getMessage());
        }
    }

    @Override
    public List<Customers> addAll(List<Customers> customers) {
        return customersRepository.addAll(customers);
    }

    @Override
    public void remove(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String usernameByCustomer = usersRepository.getById(customersRepository.getById(id).getUser_id()).getUsername();
        if (currentUsername.equals(usernameByCustomer)) {
            customersRepository.remove(id);
        }
        else {
            throw new RuntimeException("Only a customer can delete themselves");
        }
    }
}
