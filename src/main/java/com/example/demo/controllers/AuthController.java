package com.example.demo.controllers;


import com.example.demo.Services.AnonymousService;
import com.example.demo.modle.*;
import com.example.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AnonymousService anonymousService;

    @Autowired
     AuthenticationManager authenticationManager;

    @Autowired
     JwtUtils jwtUtils;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) throws InvalidPasswordException, UserNotFoundException{
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            Users user = anonymousService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return new ResponseEntity<>(new JwtResponse(user,jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password" , HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/rest-password")
    public ResponseEntity restPassword(@RequestBody ForgotPassword forgotPassword){
        boolean isRestPass = anonymousService.forgotPassword(forgotPassword.getEmail());
        if (isRestPass)
        return new  ResponseEntity ("Email sent successfully with the new password.",HttpStatus.OK);

        return new ResponseEntity("No user was found registered in the email: "+forgotPassword.getEmail(), HttpStatus.NOT_FOUND);
    }



    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameAvailability(@PathVariable String username) {
        boolean isUsernameTaken = anonymousService.isUsernameTaken(username);

        return ResponseEntity.ok(isUsernameTaken);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailAvailability(@PathVariable String email) {
        boolean isEmailTaken = anonymousService.isEmailTaken(email);

        return ResponseEntity.ok(isEmailTaken);
    }

    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<Boolean> checkPhoneAvailability(@PathVariable String phone) {
        boolean isPhoneTaken = anonymousService.isPhoneTaken(phone);

        return ResponseEntity.ok(isPhoneTaken);
    }

    @GetMapping("/check-creditCard/{creditCard}")
    public ResponseEntity<Boolean> checkCreditCardAvailability(@PathVariable String creditCard) {
        boolean isCreditCardTaken = anonymousService.isCreditCardTaken(creditCard);

        return ResponseEntity.ok(isCreditCardTaken);
    }

    @GetMapping("/check-CompanyName/{name}")
    public ResponseEntity<Boolean> checkNameAvailability(@PathVariable String name) {
        boolean isNameTaken = anonymousService.isNameTaken(name);

        return ResponseEntity.ok(isNameTaken);
    }



    @PostMapping("/register/customer")
    public ResponseEntity registerCustomer(@RequestBody UserWithCustomerDTO userWithCustomerDTO) {
        try {
            UserWithCustomerDTO createNewCustomer = anonymousService.createNewCustomer(userWithCustomerDTO);
            return new ResponseEntity<> (createNewCustomer,HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException | EmailAlreadyRegisteredException | WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/airline-company")
    public ResponseEntity registerAirlineCompany(@RequestBody UserWithAirlineCompanyDTO airlineCompanyDTO) {
        try {
            UserWithAirlineCompanyDTO createNewAirlineCompany = anonymousService.createNewAirlineCompany(airlineCompanyDTO);
            return new ResponseEntity<> (createNewAirlineCompany,HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException | EmailAlreadyRegisteredException | WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/administrator")
    public ResponseEntity registerAdministrator(@RequestBody UserWithAdministratorDTO administratorDTO) {
        try {
            UserWithAdministratorDTO createNewAdministrator = anonymousService.createNewAdministrator(administratorDTO);
            return new ResponseEntity<> (createNewAdministrator,HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException | EmailAlreadyRegisteredException | WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/flights")
    public ResponseEntity<List<Flights>> getAllFlights() {
        try {
            List<Flights> flights = anonymousService.getAllFlights();
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flights> getFlightById(@PathVariable Integer id) {
        try {
            Flights flight = anonymousService.getFlightById(id);
            if (flight != null) {
                return ResponseEntity.ok(flight);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/airlines")
    public ResponseEntity<List<AirlineCompanies>> getAllAirlines() {
        List<AirlineCompanies> airlines = anonymousService.getAllAirlines();
        return ResponseEntity.ok(airlines);
    }

    @GetMapping("/airlinesWithDetails")
    public ResponseEntity<List<AirlineCompanyDetails>> getAllAirlinesWithDetails() {
        List<AirlineCompanyDetails> airlines = anonymousService.getAllAirlineWithDetails();
        return ResponseEntity.ok(airlines);
    }

    @GetMapping("/airlines/{id}")
    public ResponseEntity<AirlineCompanies> getAirlineById(@PathVariable Integer id) {
        AirlineCompanies airline = anonymousService.getAirlineById(id);
        if (airline != null) {
            return ResponseEntity.ok(airline);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/flightsByParameters/{originCountryId}/{destinationCountryId}/{date}")
    public ResponseEntity<List<Flights>> getFlightsByParameters(@PathVariable Integer originCountryId,
                                                                @PathVariable Integer destinationCountryId,
                                                                @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<Flights> flights = anonymousService.getFlightsByParameters(originCountryId, destinationCountryId, date);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/search/{date}")
    public ResponseEntity<List<FlightDetails>> searchFlights(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) String originCountry,
            @RequestParam(required = false) String destinationCountry,
            @RequestParam(required = false) Integer [] airlineIdList) {



        List<FlightDetails> flights = anonymousService.getSearchFlights(date, originCountry, destinationCountry, airlineIdList);


        if (flights.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(flights);
        }
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Countries>> getAllCountries() {
        List<Countries> countries = anonymousService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Countries> getCountryById(@PathVariable Integer id) {
        Countries country = anonymousService.getCountryById(id);
        if (country != null) {
            return ResponseEntity.ok(country);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
