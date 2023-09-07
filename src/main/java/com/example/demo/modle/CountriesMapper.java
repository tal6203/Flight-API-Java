package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesMapper implements RowMapper<Countries> {
    public Countries mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Countries(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

}
