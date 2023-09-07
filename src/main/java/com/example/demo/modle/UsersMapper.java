package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMapper implements RowMapper<Users> {
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Users(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getInt("user_role")
        );
    }
}
