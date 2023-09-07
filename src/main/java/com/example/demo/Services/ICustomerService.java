package com.example.demo.Services;

import com.example.demo.modle.*;

import java.util.List;

public interface ICustomerService {
    Tickets addTicket(Tickets ticket) throws TicketOrderNotAllowedException;
    void removeTicket(Tickets ticket);
    List<Tickets> getTicketsByCustomer(Customers customer);
    Customers getById(Integer id) throws AccessDeniedException;

    List<Customers> getAll();

    Customers add(UserWithCustomerDTO userWithCustomerDTO);

    void update(Customers customer,Integer id);

    List<Customers> addAll(List<Customers> customers);

    void remove(Integer id);

}
