package com.example.demo;

import com.example.demo.Repository.*;
import com.example.demo.Services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableConfigurationProperties(value = {RedisDetailsConfig.class})
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate, CountriesRepository countriesRepository,
										UserRolesService userRolesService, CustomerService customerService,
										AirlineCompaniesService airlineCompaniesService, FlightsService flightsService,
										TicketsService ticketsService, UsersService usersService, AnonymousService anonymousService,
										FlightsRepository flightsRepository, CustomersRepository customersRepository,
										TicketsRepository ticketsRepository, AirlineCompaniesRepository airlineCompaniesRepository,
										UsersRepository usersRepository) {
		return args -> {



		};
	}

}
