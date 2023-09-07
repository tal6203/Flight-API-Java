package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightsMapper implements RowMapper<Flights> {
    public Flights mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Flights(
                rs.getInt("id"),
                rs.getInt("airline_company_id"),
                rs.getInt("origin_country_id"),
                rs.getInt("destination_country_id"),
                rs.getTimestamp("departure_time").toLocalDateTime(),
                rs.getTimestamp("landing_time").toLocalDateTime(),
                rs.getInt("remaining_tickets")
        );
    }
}
