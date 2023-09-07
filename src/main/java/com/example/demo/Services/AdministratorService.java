package com.example.demo.Services;

import com.example.demo.Repository.AdministratorsRepository;
import com.example.demo.Repository.AirlineCompaniesRepository;
import com.example.demo.Repository.CustomersRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AdministratorService extends ServiceBase implements IAdministratorService{

    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;
    @Autowired
    private AdministratorsRepository administratorsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public List<Customers> getAllCustomers() {
        try {
            return customersRepository.getAll();
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get Customers" + e.getMessage());
        }
    }

    public List<AirlineCompanyDetails> getAllAirlineCompaniesWithDetails(){
        List <AirlineCompanyDetails> airlineCompanyDetailsList = new ArrayList<>();
        List <AirlineCompanies> airlineCompanies = airlineCompaniesRepository.getAll();
        for (AirlineCompanies airlineCompany : airlineCompanies){
            airlineCompanyDetailsList.add(new AirlineCompanyDetails(airlineCompany.getId(),airlineCompany.getName()
                    ,airlineCompaniesRepository.getCountryNameByCountryId(airlineCompany.getCountryId()),
                    airlineCompany.getUserId()));
        }
        return airlineCompanyDetailsList;
    }

    @Override
    public AirlineCompanies addAirlineCompany(UserWithAirlineCompanyDTO userWithAirlineCompanyDTO) {
        try {
            userWithAirlineCompanyDTO.getUser().setPassword(passwordEncoder.encode(userWithAirlineCompanyDTO.getUser().getPassword()));
            Users user = usersRepository.add(userWithAirlineCompanyDTO.getUser());
            AirlineCompanyDetails airlineCompany = userWithAirlineCompanyDTO.getAirlineCompany();
            airlineCompany.setUser_id(user.getId());
            AirlineCompanies airlineCompanyOriginal = new AirlineCompanies(airlineCompany.getId(),airlineCompany.getName(),
                    airlineCompaniesRepository.getCountryIdByName(airlineCompany.getCountry()),airlineCompany.getUser_id());
            return airlineCompaniesRepository.add(airlineCompanyOriginal);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to add airline: " + e.getMessage());
        }
    }

    @Override
    public Customers addCustomer(UserWithCustomerDTO userWithCustomerDTO) {
        try {
            userWithCustomerDTO.getUser().setPassword(passwordEncoder.encode(userWithCustomerDTO.getUser().getPassword()));
            Users user = usersRepository.add(userWithCustomerDTO.getUser());
            Customers customer = userWithCustomerDTO.getCustomer();
            customer.setUser_id(user.getId());
            return  customersRepository.add(customer);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to add customer: " + e.getMessage());
        }
    }

    @Override
    public void removeAirlineCompany(AirlineCompanies airlineCompany) {
        try {
             airlineCompaniesRepository.remove(airlineCompany.getId());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to remove airline company: " + e.getMessage());
        }
    }

    @Override
    public void removeCustomer(Customers customer) {
        try {
             customersRepository.remove(customer.getId());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to remove customer: " + e.getMessage());
        }
    }

    @Override
    public List<Administrators> getAll() {
        try {
            return administratorsRepository.getAll();
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get all administrators: " + e.getMessage());
        }
    }

    @Override
    public Administrators getById(Integer id) {
        try {
            return administratorsRepository.getById(id);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get administrator by id: " + e.getMessage());
        }
    }

    @Override
    public Administrators addAdministrator(UserWithAdministratorDTO userWithAdministratorDTO) {
        try {
            userWithAdministratorDTO.getUser().setPassword(passwordEncoder.encode(userWithAdministratorDTO.getUser().getPassword()));
            Users user = usersRepository.add(userWithAdministratorDTO.getUser());
            Administrators administrator = userWithAdministratorDTO.getAdministrator();
            administrator.setUser_id(user.getId());
            return administratorsRepository.add(administrator);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to add administrator: " + e.getMessage());
        }
    }

    @Override
    public void update(Administrators administrator, Integer id) {
        try {
             administratorsRepository.update(administrator,id);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to update administrator : " + e.getMessage());
        }
    }

    @Override
    public List<Administrators> addAll(List<Administrators> administrators) {
        try {
            return administratorsRepository.addAll(administrators);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to add all administrators: " + e.getMessage());
        }
    }

    @Override
    public void removeAdministrator(Integer id) {
        try {
             administratorsRepository.remove(id);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to remove administrator: " + e.getMessage());
        }
    }
}
