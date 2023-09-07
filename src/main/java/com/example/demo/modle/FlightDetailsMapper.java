package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightDetailsMapper implements RowMapper<FlightDetails> {
    public FlightDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FlightDetails(
                rs.getInt("id"),
                rs.getString("airline_company"),
                rs.getString("origin_country"),
                rs.getString("destination_country"),
                rs.getTimestamp("departure_time").toLocalDateTime(),
                rs.getTimestamp("landing_time").toLocalDateTime(),
                rs.getInt("remaining_tickets")
        );
    }
}

