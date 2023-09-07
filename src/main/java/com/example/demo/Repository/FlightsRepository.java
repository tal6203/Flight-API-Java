package com.example.demo.Repository;

import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class FlightsRepository implements IFlightsRepository{
    protected static final String FLIGHTS_TABLE_NAME = "flights";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;


    public List<FlightDetails> getFlightsByCustomerWithName(Customers customer) {
        String query = "SELECT f.id, ac.name AS airline_company, " +
                "oc.name AS origin_country, dc.name AS destination_country, " +
                "f.departure_time, f.landing_time, f.remaining_tickets " +
                "FROM flights f " +
                "JOIN tickets t ON f.id = t.flight_id " +
                "JOIN airline_companies ac ON f.airline_company_id = ac.id " +
                "JOIN countries oc ON f.origin_country_id = oc.id " +
                "JOIN countries dc ON f.destination_country_id = dc.id " +
                "WHERE t.customer_id = ?";
        try {
            return jdbcTemplate.query(query, new FlightDetailsMapper(), customer.getId());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public List<FlightDetails> getFlightDetailsByAirlineCompanyId(AirlineCompanies airlineCompany) {
        String query = "SELECT f.id, ac.name AS airline_company, " +
                "oc.name AS origin_country, dc.name AS destination_country, " +
                "f.departure_time, f.landing_time, f.remaining_tickets " +
                "FROM flights f " +
                "JOIN airline_companies ac ON f.airline_company_id = ac.id " +
                "JOIN countries oc ON f.origin_country_id = oc.id " +
                "JOIN countries dc ON f.destination_country_id = dc.id " +
                "WHERE f.airline_company_id = ?";
        try {
            return jdbcTemplate.query(query, new FlightDetailsMapper(), airlineCompany.getId());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public List<Flights> getFlightsByCustomer(Customers customer) {
        String query = "SELECT f.* FROM flights f " +
                "JOIN tickets t ON f.id = t.flight_id " +
                "WHERE t.customer_id = ?";
        try {
            return jdbcTemplate.query(query, new FlightsMapper(), customer.getId());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public List<Flights> getFlightsByOriginCountryId(Integer countryId) {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String query = "SELECT * FROM flights WHERE origin_country_id = :countryId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("countryId", countryId);
            return namedParameterJdbcTemplate.query(query, params, new FlightsMapper());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by origin country ID: " + e.getMessage());
        }
    }

    public List<Flights> getFlightsByDestinationCountryId(Integer countryId) {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String query = "SELECT * FROM flights WHERE destination_country_id = :countryId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("countryId", countryId);
            return namedParameterJdbcTemplate.query(query, params, new FlightsMapper());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by destination country ID: " + e.getMessage());
        }
    }

    public List<Flights> getFlightsByDepartureDate(Date date) {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String query = "SELECT * FROM flights WHERE DATE(departure_time) <= DATE(:date)";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("date", date);
            return namedParameterJdbcTemplate.query(query, params, new FlightsMapper());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by departure date: " + e.getMessage());
        }
    }

    public List<Flights> getFlightsByLandingDate(Date date) {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String query = "SELECT * FROM flights WHERE DATE(landing_time) = DATE(:date)";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("date", date);
            return namedParameterJdbcTemplate.query(query, params, new FlightsMapper());
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get flights by landing date: " + e.getMessage());
        }
    }
    public List<Flights> getFlightsByParameters(Integer originCountryId, Integer destinationCountryId, Date date) {
        String query = "SELECT * FROM get_flights_by_parameters(?, ?, ?)";
        try {
            return jdbcTemplate.query(query, new FlightsMapper(), originCountryId, destinationCountryId, date);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }


    public List<Flights> getFlightsByAirlineId(Integer airlineId) {
        String query = String.format("SELECT * FROM %s where airline_company_id = ?",FLIGHTS_TABLE_NAME);
        try {
            return jdbcTemplate.query(query, new FlightsMapper(), airlineId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }


    public List<Flights> getArrivalFlights(Integer countryId) {
        String query = "SELECT * FROM flights WHERE destination_country_id = ? AND landing_time <= ?";
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime next12Hours = now.plusHours(12);

            return jdbcTemplate.query(query, new FlightsMapper(), countryId, Timestamp.valueOf(next12Hours));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Flights> getSearchFlights(Date date, Integer originCountryId, Integer destinationCountryId, List<Integer> airlineIds) {
        String query = "SELECT * FROM flights WHERE departure_time >= ?";
        List<Object> queryParams = new ArrayList<>();
        queryParams.add(date);

        if (originCountryId != null) {
            query += " AND origin_country_id = ?";
            queryParams.add(originCountryId);
        }

        if (destinationCountryId != null) {
            query += " AND destination_country_id = ?";
            queryParams.add(destinationCountryId);
        }

        if (airlineIds != null && !airlineIds.isEmpty()) {

            query += " AND airline_company_id IN (";
            for (int i = 0; i < airlineIds.size(); i++) {
                query += "?";
                queryParams.add(airlineIds.get(i));
                if (i < airlineIds.size() - 1) {
                    query += ",";
                }
            }
            query += ")";
        }



        try {
            Object[] queryParamsArray = queryParams.toArray();
            return jdbcTemplate.query(query, new FlightsMapper(), queryParamsArray);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }




    public List<Flights> getDepartureFlights(Integer countryId) {
        String query = "SELECT * FROM flights WHERE origin_country_id = ? AND departure_time <= ?";
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime next12Hours = now.plusHours(12);

            return jdbcTemplate.query(query, new FlightsMapper(), countryId, Timestamp.valueOf(next12Hours));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void decreaseRemainingTickets(Integer flightId) {
        String query = "UPDATE flights SET remaining_tickets = remaining_tickets - 1 WHERE id = ?";
        jdbcTemplate.update(query, flightId);
    }

    public void incrementRemainingTickets(Integer flightId) {
        String query = "UPDATE flights SET remaining_tickets = remaining_tickets + 1 WHERE id = ?";
        jdbcTemplate.update(query, flightId);
    }

    @Override
    public Flights getById(Integer id) {
        String query = String.format("Select * from %s where id=?", FLIGHTS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new FlightsMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flights> getAll() {
        String query = String.format("Select * from %s", FLIGHTS_TABLE_NAME);
        return jdbcTemplate.query(query, new FlightsMapper());
    }

    @Override
    public Flights add(Flights flight) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (airline_company_id, origin_country_id, destination_country_id, " +
                "departure_time, landing_time, remaining_tickets) " +
                "VALUES (:airline_company_id, :origin_country_id, :destination_country_id, :departure_time, " +
                ":landing_time, :remaining_tickets)", FLIGHTS_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("airline_company_id", flight.getAirlineCompanyId());
        params.addValue("origin_country_id", flight.getOriginCountryId());
        params.addValue("destination_country_id", flight.getDestinationCountryId());
        params.addValue("departure_time", flight.getDepartureTime());
        params.addValue("landing_time", flight.getLandingTime());
        params.addValue("remaining_tickets", flight.getRemainingTickets());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        flight.setId(id);
        return flight;
    }

    @Override
    public void update(Flights flight,Integer id) {
        String query = String.format("UPDATE %s SET airline_company_id=?, origin_country_id=?, destination_country_id=?, departure_time=?, landing_time=?," +
                "remaining_tickets=?  WHERE id= ?", FLIGHTS_TABLE_NAME);
        jdbcTemplate.update(query, flight.getAirlineCompanyId(), flight.getOriginCountryId(),flight.getDestinationCountryId(),
                flight.getDepartureTime(),flight.getLandingTime(),flight.getRemainingTickets(),id);
    }

    @Override
    public List<Flights> addAll(List<Flights> flights) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (airline_company_id, origin_country_id, destination_country_id, " +
                "departure_time, landing_time, remaining_tickets) " +
                "VALUES (:airline_company_id, :origin_country_id, :destination_country_id, :departure_time, " +
                ":landing_time, :remaining_tickets)", FLIGHTS_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[flights.size()];
        for (int i = 0; i < flights.size(); i++) {
            Flights flight = flights.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("airline_company_id", flight.getAirlineCompanyId());
            params.addValue("origin_country_id", flight.getOriginCountryId());
            params.addValue("destination_country_id", flight.getDestinationCountryId());
            params.addValue("departure_time", flight.getDepartureTime());
            params.addValue("landing_time", flight.getLandingTime());
            params.addValue("remaining_tickets", flight.getRemainingTickets());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return flights;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", FLIGHTS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
