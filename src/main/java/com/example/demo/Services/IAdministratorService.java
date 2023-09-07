package com.example.demo.Services;

import com.example.demo.modle.*;

import java.util.List;

public interface IAdministratorService {
    List<Customers> getAllCustomers();
    AirlineCompanies addAirlineCompany(UserWithAirlineCompanyDTO userWithAirlineCompanyDTO);
    Customers addCustomer(UserWithCustomerDTO userWithCustomerDTO);
    void removeAirlineCompany(AirlineCompanies airlineCompany);
    void removeCustomer(Customers customer);

    List<Administrators> getAll();

    Administrators getById(Integer id);
    Administrators addAdministrator(UserWithAdministratorDTO userWithAdministratorDTO);

    void update(Administrators administrator,Integer id);

    List<Administrators> addAll(List<Administrators> administrators);

    void removeAdministrator(Integer id);
}
