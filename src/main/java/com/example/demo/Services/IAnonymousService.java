package com.example.demo.Services;

import com.example.demo.modle.*;

public interface IAnonymousService {
    Users login(String username, String password) throws UserNotFoundException, InvalidPasswordException;
    UserWithCustomerDTO createNewCustomer(UserWithCustomerDTO userWithCustomerDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException;
    UserWithAirlineCompanyDTO createNewAirlineCompany(UserWithAirlineCompanyDTO userWithAirlineCompanyDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException;
    UserWithAdministratorDTO createNewAdministrator(UserWithAdministratorDTO userWithAdministratorDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException;
}


