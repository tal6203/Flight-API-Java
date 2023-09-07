package com.example.demo.Repository;

import com.example.demo.modle.Tickets;
import com.example.demo.modle.TicketsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
public class TicketsRepository implements ITicketsRepository{
    protected static final String TICKETS_TABLE_NAME = "tickets";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    public List<Tickets> getTicketsByFlight(Integer flightId) {
        String query = String.format("SELECT * FROM %s WHERE flight_id = ?",TICKETS_TABLE_NAME);
        try {
            return jdbcTemplate.query(query, new TicketsMapper(), flightId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public List<Tickets> getTicketsByCustomer(Integer customerId) {
        String query = String.format("SELECT * FROM %s WHERE customer_id = ?",TICKETS_TABLE_NAME);
        try {
            return jdbcTemplate.query(query, new TicketsMapper(), customerId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }



    @Override
    public Tickets getById(Integer id) {
        String query = String.format("Select * from %s where id=?", TICKETS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new TicketsMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Tickets getTicktByFlightIdAndCustomerId(Integer flightId,Integer customerId) {
        String query = String.format("Select * from %s where flight_id=? and customer_id=?", TICKETS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new TicketsMapper(), flightId,customerId);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Tickets> getAll() {
        String query = String.format("Select * from %s", TICKETS_TABLE_NAME);
        return jdbcTemplate.query(query, new TicketsMapper());
    }

    @Override
    public Tickets add(Tickets ticket) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (flight_id, customer_id) " +
                "VALUES (:flight_id, :customer_id)", TICKETS_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("flight_id", ticket.getFlightId());
        params.addValue("customer_id", ticket.getCustomerId());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        ticket.setId(id);
        return ticket;
    }

    @Override
    public void update(Tickets ticket,Integer id) {
        String query = String.format("UPDATE %s SET flight_id=?, customer_id=? WHERE id= ?", TICKETS_TABLE_NAME);
        jdbcTemplate.update(query, ticket.getFlightId(), ticket.getCustomerId(),id);
    }

    @Override
    public List<Tickets> addAll(List<Tickets> tickets) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (flight_id, customer_id) " +
                "VALUES (:flight_id, :customer_id)", TICKETS_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            Tickets ticket = tickets.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("flight_id", ticket.getFlightId());
            params.addValue("customer_id", ticket.getCustomerId());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return tickets;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", TICKETS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
