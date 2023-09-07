package com.example.demo.modle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AirlineCompanyDetailsMapper implements RowMapper<AirlineCompanyDetails>{
    public AirlineCompanyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AirlineCompanyDetails(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("country"),
                rs.getInt("user_id")
        );
    }
}
