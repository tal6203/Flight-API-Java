package com.example.demo.Repository;

import com.example.demo.modle.AirlineCompanies;
import com.example.demo.modle.AirlineCompaniesMapper;
import com.example.demo.modle.AirlineCompanyDetails;
import com.example.demo.modle.FlightDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class AirlineCompaniesRepository implements IAirlineCompaniesRepository{
    protected static final String AIRLINECOMPANIES_TABLE_NAME = "airline_companies";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    public AirlineCompanies getAirlineByUsername(String username) {
        String query = "SELECT airline.id, airline.name, airline.country_id, airline.user_id " +
                "FROM airline_companies AS airline " +
                "INNER JOIN users ON airline.user_id = users.id " +
                "WHERE users.username = ?";
        try {
            return jdbcTemplate.queryForObject(query,new AirlineCompaniesMapper(),username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public boolean existsByName(String name) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE name = ?",AIRLINECOMPANIES_TABLE_NAME);
        int count = jdbcTemplate.queryForObject(query, Integer.class, name);
        return count == 0;
    }

    public AirlineCompanyDetails getAirlineByUsernameShowAllDetails(String username) {
        String query = "SELECT airline.id, airline.name, airline.country_id, airline.user_id " +
                "FROM airline_companies AS airline " +
                "INNER JOIN users ON airline.user_id = users.id " +
                "WHERE users.username = ?";;
        try {
           AirlineCompanies airlineCompany = jdbcTemplate.queryForObject(query,new AirlineCompaniesMapper(),username);

           return new AirlineCompanyDetails(airlineCompany.getId(),airlineCompany.getName(),getCountryNameByCountryId(airlineCompany.getCountryId()),airlineCompany.getUserId());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<AirlineCompanies> getAirlinesByCountry(int countryId) {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String query = "SELECT * FROM airline_companies WHERE country_id = :countryId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("countryId", countryId);
            return namedParameterJdbcTemplate.query(query, params, new AirlineCompaniesMapper());
        }
        catch (DataAccessException e) {
            // Handle any exceptions
            throw new RuntimeException("Failed to get airlines by country: " + e.getMessage());
        }
    }

    public AirlineCompanies getAirlineCompanyByName(String nameComapny){
        String query = String.format("SELECT * FROM %s WHERE name = ?",AIRLINECOMPANIES_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new AirlineCompaniesMapper(),nameComapny);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    @Override
    public AirlineCompanies getById(Integer id) {
        String query = String.format("Select * from %s where id=?", AIRLINECOMPANIES_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new AirlineCompaniesMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<AirlineCompanies> getAll() {
        String query = String.format("Select * from %s", AIRLINECOMPANIES_TABLE_NAME);
        return jdbcTemplate.query(query, new AirlineCompaniesMapper());
    }

    @Override
    public AirlineCompanies add(AirlineCompanies airlineCompany) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (name, country_id, user_id) " +
                "VALUES (:name, :country_id, :user_id)", AIRLINECOMPANIES_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", airlineCompany.getName());
        params.addValue("country_id", airlineCompany.getCountryId());
        params.addValue("user_id", airlineCompany.getUserId());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        airlineCompany.setId(id);

        return airlineCompany;
    }

    @Override
    public void update(AirlineCompanies airlineCompany, Integer id) {
        String query = String.format("UPDATE %s SET name=?, country_id=?, user_id=? WHERE id= ?", AIRLINECOMPANIES_TABLE_NAME);
        jdbcTemplate.update(query, airlineCompany.getName(), airlineCompany.getCountryId(), airlineCompany.getUserId(),id);
    }

    @Override
    public List<AirlineCompanies> addAll(List<AirlineCompanies> airlineCompanies) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (name, country_id, user_id) " +
                "VALUES (:name, :country_id, :user_id)", AIRLINECOMPANIES_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[airlineCompanies.size()];
        for (int i = 0; i < airlineCompanies.size(); i++) {
            AirlineCompanies airlineCompany = airlineCompanies.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", airlineCompany.getName());
            params.addValue("country_id", airlineCompany.getCountryId());
            params.addValue("user_id", airlineCompany.getUserId());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);

        return airlineCompanies;
    }

    public void updateFlightByAirlineCompanyId(Integer airlineCompanyId,Integer flightId ,FlightDetails flight) {
        // Lookup country IDs by country names
        Integer originCountryId = getCountryIdByName(flight.getOriginCountry());
        Integer destinationCountryId = getCountryIdByName(flight.getDestinationCountry());

        if (originCountryId == -1 || destinationCountryId == -1) {
            throw new RuntimeException("Invalid origin or destination country name");
        }

        String query = "UPDATE flights " +
                "SET departure_time = ?, landing_time = ?, remaining_tickets = ?, " +
                "origin_country_id = ?, destination_country_id = ? " +
                "WHERE id = ? AND airline_company_id = ?";
        try {
            jdbcTemplate.update(
                    query,
                    flight.getDepartureTime(),
                    flight.getLandingTime(),
                    flight.getRemainingTickets(),
                    originCountryId,           // Set the resolved origin country ID
                    destinationCountryId,      // Set the resolved destination country ID
                    flightId,
                    airlineCompanyId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to update flight: " + e.getMessage());
        }
    }

    public Integer getCountryIdByName(String countryName) {
        String query = "SELECT id FROM countries WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, countryName);
        } catch (EmptyResultDataAccessException e) {
            return -1; // Indicate that country name was not found
        }
    }

    public String getCountryNameByCountryId(Integer countryId){
        String query = "SELECT name FROM countries WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, countryId);
        } catch (EmptyResultDataAccessException e) {
            return "-1";
        }
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", AIRLINECOMPANIES_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
