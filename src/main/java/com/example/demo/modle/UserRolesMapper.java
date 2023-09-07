package com.example.demo.modle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRolesMapper implements RowMapper<UserRoles> {
    public UserRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserRoles(
                rs.getInt("id"),
                Roles.valueOf(rs.getString("role_name"))
        );
    }
}
