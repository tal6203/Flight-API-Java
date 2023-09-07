package com.example.demo.Services;

import com.example.demo.Repository.AirlineCompaniesRepository;
import com.example.demo.Repository.CountriesRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.modle.*;
import com.example.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AirlineCompaniesService extends ServiceBase implements IAirlineCompaniesService {
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UsersRepository usersRepository;
    @Value("${min_RemainingTickets}")
    private Integer min_RemainingTickets;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Flights> getFlightsByAirlineId(Integer id) {
        try {
            return flightsRepository.getFlightsByAirlineId(id);
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by airline: " + e.getMessage());
        }
    }
    public AirlineCompanies getAirlineCompanyByUsername(String username){
        try{
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineCompanyUsername = usersRepository.getByUsername(username).getUsername();
            if (currentUsername.equals(airlineCompanyUsername)) {
                return airlineCompaniesRepository.getAirlineByUsername(username);
            }
            else {
                throw new RuntimeException("Only an airline that is connected can only receive information about itself");
            }
        }catch (Exception e){
            throw new RuntimeException("Not found Airline company by username: "+ username);
        }
    }


    public AirlineCompanyDetails getAirlineByUsernameShowAllDetails(String username){
        try{
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineCompanyUsername = usersRepository.getByUsername(username).getUsername();
            if (currentUsername.equals(airlineCompanyUsername)) {
                return airlineCompaniesRepository.getAirlineByUsernameShowAllDetails(username);
            }
            else {
                throw new RuntimeException("Only an airline that is connected can only receive information about itself");
            }
        }catch (Exception e){
            throw new RuntimeException("Not found Airline company by username: "+ username);
        }
    }

    public List<FlightDetails> getFlightsDetailsByUsername(String username) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineCompanyUsername = usersRepository.getByUsername(username).getUsername();
            AirlineCompanies airlineCompany = airlineCompaniesRepository.getAirlineByUsername(username);
            if (airlineCompanyUsername.equals(currentUsername)) {
                return flightsRepository.getFlightDetailsByAirlineCompanyId(airlineCompany);
            }
            else {
                throw new IncompatibleAirlineException("An airline can only receive information about its flights");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by airline: " + e.getMessage());
        }
    }



    public Flights addFlight(Flights flight) {
        try {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineCompanyUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineCompanyUsername.equals(currentUsername)) {
           return flightsRepository.add(flight);
        } else {
            throw new IncompatibleAirlineException("An airline to manufacture a flight that is its own");
        }

        } catch(Exception e){
        // Handle any exceptions
        throw new RuntimeException("Failed to update flight: " + e.getMessage());
    }
}





    public void updateFlight(Flights flight, Integer flightId) {
        try {
            String airlineCompanyUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
             if (airlineCompanyUsername.equals(currentUsername)) {
                 flightsRepository.update(flight, flightId);
             }
             else {
                 throw new IncompatibleAirlineException("Only flights belonging to the same airline can be edited");
             }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to update flight: " + e.getMessage());
        }
    }

    public void UpdateFlightsDetailsByUsername(String username,Integer flightId,FlightDetails flightDetails) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineCompanyUsername = usersRepository.getByUsername(username).getUsername();
            String airlineCompany1 = airlineCompaniesRepository.getAirlineByUsername(currentUsername).getName();
            String airlineCompany2 = airlineCompaniesRepository.getAirlineCompanyByName(flightDetails.getAirlineCompany()).getName();

            if (currentUsername.equals(airlineCompanyUsername)) {
                if (airlineCompany1.equals(airlineCompany2)) {
                    AirlineCompanies airlineCompany = airlineCompaniesRepository.getAirlineByUsername(username);

                    validateFlight(flightDetails);
                    airlineCompaniesRepository.updateFlightByAirlineCompanyId(airlineCompany.getId(),flightId ,flightDetails);
                }
                else {
                    throw new IncompatibleAirlineException("An airline can only update flights that belong to it");
                }
            }
            else {
                throw new IncompatibleAirlineException("An airline can only update flights that belong to it");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by airline: " + e.getMessage());
        }
    }

    private void validateFlight(FlightDetails flight) {
        if (flight.getRemainingTickets() < min_RemainingTickets) {
            throw new IllegalArgumentException("Number of remaining tickets cannot be negative");
        }

        if (flight.getLandingTime().isBefore(flight.getDepartureTime()) || flight.getLandingTime().isEqual(flight.getDepartureTime())) {
            throw new IllegalArgumentException("Landing time cannot be before departure time");
        }

        if (flight.getOriginCountry().equals(flight.getDestinationCountry())) {
            throw new IllegalArgumentException("Origin and destination country cannot be the same");
        }
    }

    @Override
    public AirlineCompanies addAirlineCompany(UserWithAirlineCompanyDTO userWithAirlineCompanyDTO) {
        userWithAirlineCompanyDTO.getUser().setPassword(passwordEncoder.encode(userWithAirlineCompanyDTO.getUser().getPassword()));
     Users user = usersRepository.add(userWithAirlineCompanyDTO.getUser());
        AirlineCompanyDetails airlineCompany = userWithAirlineCompanyDTO.getAirlineCompany();
        airlineCompany.setUser_id(user.getId());
        AirlineCompanies createdAirlineCompany = new AirlineCompanies(airlineCompany.getId(),airlineCompany.getName(),
                airlineCompaniesRepository.getCountryIdByName(airlineCompany.getCountry()),airlineCompany.getUser_id());
       return airlineCompaniesRepository.add(createdAirlineCompany);
    }

    @Override
    public void updateAirlineCompany (AirlineCompanies airlineCompany, Integer id) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineUsername =  usersRepository.getById(airlineCompaniesRepository.getById(id).getUserId()).getUsername();
                if (currentUsername.equals(airlineUsername)) {
                    airlineCompaniesRepository.update(airlineCompany, id);
                }
                else {
                    throw new RuntimeException("Only an airline can update itself");
                }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to update airline company: " + e.getMessage());
        }
    }

    public void updateAirlineCompanywithDetails(String username,Integer id, AirlineCompanyDetails airlineCompanyDetails){
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineUsername = usersRepository.getByUsername(username).getUsername();
            if (currentUsername.equals(airlineUsername)){
                AirlineCompanies airlineCompany = new AirlineCompanies(airlineCompanyDetails.getId(),airlineCompanyDetails.getName(),
                        airlineCompaniesRepository.getCountryIdByName(airlineCompanyDetails.getCountry()),airlineCompanyDetails.getUser_id());
                airlineCompaniesRepository.update(airlineCompany,id);
            }
            else {
                throw new RuntimeException("An airline can only update its own details");
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to update airline company: " + e.getMessage());
        }
    }

    @Override
    public List<AirlineCompanies> addAllAirlineCompanies(List<AirlineCompanies> airlineCompanies) {
        return airlineCompaniesRepository.addAll(airlineCompanies);
    }

    @Override
    public void removeAirlineCompany(Integer id) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineUsername =  usersRepository.getById(airlineCompaniesRepository.getById(id).getUserId()).getUsername();
            if (currentUsername.equals(airlineUsername)) {
                airlineCompaniesRepository.remove(id);
            }
            else {
                throw new RuntimeException("Only an airline can remove itself");
            }
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to update airline company: " + e.getMessage());
        }
    }
}

