package com.example.demo;

import com.example.demo.Services.*;
import com.example.demo.modle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


// It is important to note that before running the tests, all lines of
// code in the functions that use the tests that check Authentication must be commented out.

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	 AirlineCompaniesService airlineCompaniesService;
	@Autowired
	 FlightsService flightsService;

	@Autowired
	 TicketsService ticketsService;

	@Autowired
	 CountriesService countriesService;
	@Autowired
	 UsersService usersService;
	@Autowired
	 UserRolesService userRolesService;
	@Autowired
	 CustomerService customerService;
	@Autowired
	 JdbcTemplate jdbcTemplate;

	 Users user1;
	 Users user2;
	  Users user3;
	  Users user4;
	 AirlineCompanyDetails airlineCompany;
	AirlineCompanies airlineCompanyCreated;
	 Customers customer1;
	 Customers customer2;
	  Customers customer3;


	@BeforeEach
	void initData() {
		deleteTestData();

		// Create users
		user1 = new Users(0, "username_1", "password1", "email1@gmail.com", 1);
		user2 = new Users(0, "username_2", "password2", "email2@gmail.com", 1);
		user3 = new Users(0, "username_3", "password3", "email3@gmail.com", 1);
		user4 = new Users(0, "username_4", "password4", "email4@gmail.com", 2);





		// Create airline company
	 airlineCompany = new AirlineCompanyDetails(0, "Test1 Airline", "Israel", user4.getId());

		// Add airline company to the database
	 airlineCompanyCreated = airlineCompaniesService.addAirlineCompany(new UserWithAirlineCompanyDTO(user4,airlineCompany));

		// Create customers
		customer1 = new Customers(0, "John", "Doe", "john@example.com", "052-7773312", "4522-5555-5555-0001", user1.getId());
		customer2 = new Customers(0, "Michel", "Levi", "michel@example.com", "052-2358312", "4522-6666-4444-0001", user2.getId());
		customer3 = new Customers(0, "Alon", "Ran", "alon@example.com", "052-7794723", "4522-6666-1234-9999", user3.getId());

		// Add customers to the database
		customerService.add(new UserWithCustomerDTO(user1,customer1));
		customerService.add(new UserWithCustomerDTO(user2,customer2));
		customerService.add(new UserWithCustomerDTO(user3,customer3));
		// Code to initialize other test data in the database
		// This can include creating flights, airline companies, customers, etc.
	}

	private void deleteTestData() {
		jdbcTemplate.execute("CALL delete_test_data()");
	}

	@Test
	void testCreateAirlineCompany() {

//		// Retrieve the airline company from the database
		AirlineCompanies savedAirlineCompany = airlineCompaniesService.getAirlineById(airlineCompanyCreated.getId());



//		// Assert that the airline company was created correctly
		assertEquals(airlineCompanyCreated.getName(), savedAirlineCompany.getName());
		assertEquals(1, savedAirlineCompany.getCountryId());
	}

	@Test
	void testCreateFlightAndTickets() {
		// Create a flight
		Flights flight = new Flights(0,airlineCompanyCreated.getId(),1,2, LocalDateTime.now(), LocalDateTime.now().plusHours(2),10);
		// Set other flight properties
		flightsService.addFlight(flight);


		// Create tickets for the flight
		Tickets ticket1 = new Tickets(0,flight.getId(),customer1.getId());

		// Set other ticket properties
		ticketsService.add(ticket1);

		Tickets ticket2 = new Tickets(0,flight.getId(),customer2.getId());

		// Set other ticket properties
		ticketsService.add(ticket2);

		// Retrieve the tickets for the flight

		List<Tickets> flightTickets = ticketsService.getTicketsByFlight(flight.getId());



		// Assert that the tickets were added correctly
		assertEquals(2, flightTickets.size());

	}

	@Test
	void testCreateFlightWithIllogicalDates() {

		// Attempt to create a flight with illogical dates
		Flights flight = new Flights(0,airlineCompanyCreated.getId(),1,2,LocalDateTime.now().plusDays(1),LocalDateTime.now(),5);

		// Set other flight properties
		assertThrows(IllegalArgumentException.class, () -> flightsService.addFlight(flight));
	}

	@Test
	void testCreateFlightWithNegativeTickets() {
		// Attempt to create a flight with a negative number of tickets
		Flights flight = new Flights(0,airlineCompanyCreated.getId(),1,2,LocalDateTime.now(),LocalDateTime.now().plusDays(1),-10);
		// Set other flight properties
		assertThrows(IllegalArgumentException.class, () -> flightsService.addFlight(flight));
	}

	@Test
	void testPurchaseTicketForOverbookedFlight() {
		// Create a flight with no available tickets
		Flights flight = new Flights(0,airlineCompanyCreated.getId(),1,2,LocalDateTime.now(),LocalDateTime.now().plusDays(1),0);
		flightsService.addFlight(flight);
		// Attempt to purchase a ticket for the overbooked flight
		Tickets ticket = new Tickets(0,flight.getId(),customer3.getId());
		// Set other ticket properties
		assertThrows(FlightFullyBookedException.class, () -> ticketsService.add(ticket));

	}
}
