package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AirlineCompaniesMapper implements RowMapper<AirlineCompanies> {
    public AirlineCompanies mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AirlineCompanies(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("country_id"),
                rs.getInt("user_id")
        );
    }
}
