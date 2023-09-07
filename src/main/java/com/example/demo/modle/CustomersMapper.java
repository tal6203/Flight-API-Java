package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersMapper implements RowMapper<Customers> {
    public Customers mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customers(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("address"),
                rs.getString("phone_no"),
                rs.getString("credit_card_no"),
                rs.getInt("user_id")
        );
    }
}
