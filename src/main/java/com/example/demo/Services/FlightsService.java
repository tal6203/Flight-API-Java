package com.example.demo.Services;

import com.example.demo.Repository.AirlineCompaniesRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.modle.AccessDeniedException;
import com.example.demo.modle.Customers;
import com.example.demo.modle.FlightDetails;
import com.example.demo.modle.Flights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FlightsService implements IFlightsService {

    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;

    @Value("${min_RemainingTickets}")
    private Integer min_RemainingTickets;

    @Override
    public Flights getFlightById(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flightsRepository.getById(id).getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineUsername.equals(currentUsername)) {
            return flightsRepository.getById(id);
        }
        throw new RuntimeException("An airline can see details of a flight you belong to");
    }


    @Override
    public List<Flights> getAllFlights() {
        return flightsRepository.getAll();
    }

    public  Flights addFlightWithDetails(FlightDetails flightDetails){
        Flights flight = new Flights(flightDetails.getId(),airlineCompaniesRepository.getAirlineByUsername(flightDetails.getAirlineCompany()).getId()
                ,airlineCompaniesRepository.getCountryIdByName(flightDetails.getOriginCountry()),airlineCompaniesRepository.getCountryIdByName(flightDetails.getDestinationCountry()),flightDetails.getDepartureTime(),flightDetails.getLandingTime(),
                flightDetails.getRemainingTickets());
            validateFlight(flight);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineUsername.equals(currentUsername)) {
            return flightsRepository.add(flight);
        }
        throw new RuntimeException("An airline can add a flight you belonged to");
    }

    @Override
    public Flights addFlight(Flights flight) {
        validateFlight(flight);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineUsername.equals(currentUsername)) {
            return flightsRepository.add(flight);
        }
        throw new RuntimeException("An airline can add a flight you belonged to");
    }

    @Override
    public void decreaseRemainingTickets(Integer flightId) {
        Flights flight = flightsRepository.getById(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }

        flightsRepository.decreaseRemainingTickets(flightId);
    }

    @Override
    public void incrementRemainingTickets(Integer flightId) {
        Flights flight = flightsRepository.getById(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }

        flightsRepository.incrementRemainingTickets(flightId);
    }

    @Override
    public void updateFlight(Flights flight, Integer id) {
        validateFlight(flight);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineUsername.equals(currentUsername)) {
            flightsRepository.update(flight, id);
        }
        throw new RuntimeException("An airline can add a flight you belonged to");
    }

    @Override
    public List<Flights> addAllFlights(List<Flights> flights) {
        for (Flights flight : flights) {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getUserId()).getUsername();
            if (airlineUsername.equals(currentUsername)) {
                validateFlight(flight);
            }
            throw new RuntimeException("An airline can add a flight you belonged to");
        }
       return flightsRepository.addAll(flights);
    }

    @Override
    public void removeFlight(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineUsername = usersRepository.getById(airlineCompaniesRepository.getById(flightsRepository.getById(id).getAirlineCompanyId()).getUserId()).getUsername();
        if (airlineUsername.equals(currentUsername)) {
            flightsRepository.remove(id);
        }
        throw new RuntimeException("Only a flight belonging to the same airline can be deleted");
    }

    public List<Flights> getFlightsByCustomer(Customers customer) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String CustmerUsername = usersRepository.getById(customer.getUser_id()).getUsername();
        if (CustmerUsername.equals(currentUsername)) {
            Customers exists_customer = customerService.getById(customer.getId());
            if (exists_customer != null) {
                return flightsRepository.getFlightsByCustomer(customer);
            } else {
                throw new NoSuchElementException("Customer with ID " + customer.getId() + " does not exist");
            }
        }
        throw new RuntimeException("Information about flights can only be given to that customer who purchased a ticket for them");
    }


    public List<FlightDetails> getFlightsByUsername(String username) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String CustomerUsername = usersRepository.getByUsername(username).getUsername();
        if (CustomerUsername.equals(currentUsername)) {
            Customers exists_customer = customerService.getCustomerByUsername(username);
            if (exists_customer != null) {
                return flightsRepository.getFlightsByCustomerWithName(exists_customer);
            } else {
                throw new NoSuchElementException("Customer with username " + username + " does not exist");
            }
        }
        throw new RuntimeException("Information about flights can only be given to that customer who purchased a ticket for them");
    }

    public List<Flights> getFlightsByOriginCountryId(Integer countryId) {
        return flightsRepository.getFlightsByOriginCountryId(countryId);
    }

    public List<Flights> getFlightsByDestinationCountryId(Integer countryId) {
        return flightsRepository.getFlightsByDestinationCountryId(countryId);
    }

    public List<Flights> getFlightsByDepartureDate(Date date) {
        return flightsRepository.getFlightsByDepartureDate(date);
    }

    public List<Flights> getFlightsByLandingDate(Date date) {
        return flightsRepository.getFlightsByLandingDate(date);
    }

    public List<Flights> getFlightsByParameters(Integer originCountryId, Integer destinationCountryId, Date date) {
        if (originCountryId.equals(destinationCountryId)) {
            throw new IllegalArgumentException("Origin and destination country cannot be the same");
        }
        return flightsRepository.getFlightsByParameters(originCountryId, destinationCountryId, date);
    }

    public List<Flights> getFlightsByAirlineId(Integer airlineId) {
        return flightsRepository.getFlightsByAirlineId(airlineId);
    }

    public List<Flights> getArrivalFlights(Integer countryId) {
        return flightsRepository.getArrivalFlights(countryId);
    }

    public List<Flights> getDepartureFlights(Integer countryId) {
        return flightsRepository.getDepartureFlights(countryId);
    }

    private void validateFlight(Flights flight) {
        if (flight.getRemainingTickets() < min_RemainingTickets) {
            throw new IllegalArgumentException("Number of remaining tickets cannot be negative");
        }

        if (flight.getLandingTime().isBefore(flight.getDepartureTime()) || flight.getLandingTime().isEqual(flight.getDepartureTime())) {
            throw new IllegalArgumentException("Landing time cannot be before departure time");
        }

        if (flight.getOriginCountryId() == flight.getDestinationCountryId()) {
            throw new IllegalArgumentException("Origin and destination country cannot be the same");
        }
    }
}
