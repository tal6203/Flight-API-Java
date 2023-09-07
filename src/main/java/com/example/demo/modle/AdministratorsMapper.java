package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorsMapper implements RowMapper<Administrators> {
    public Administrators mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Administrators(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("user_id")
        );
    }
}
