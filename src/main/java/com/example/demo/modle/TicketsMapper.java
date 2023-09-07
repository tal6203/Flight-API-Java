package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketsMapper implements RowMapper<Tickets> {
    public Tickets mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tickets(
                rs.getInt("id"),
                rs.getInt("flight_id"),
                rs.getInt("customer_id")
        );
    }
}
