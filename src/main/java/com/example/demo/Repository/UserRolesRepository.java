package com.example.demo.Repository;

import com.example.demo.modle.UserRoles;
import com.example.demo.modle.UserRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class UserRolesRepository implements IUserRoleRepository{
    protected static final String USERROLES_TABLE_NAME = "user_roles";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    @Override
    public UserRoles getById(Integer id) {
        String query = String.format("Select * from %s where id=?", USERROLES_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new UserRolesMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UserRoles> getAll() {
        String query = String.format("Select * from %s", USERROLES_TABLE_NAME);
        return jdbcTemplate.query(query, new UserRolesMapper());
    }

    @Override
    public UserRoles add(UserRoles userRole) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (role_name) VALUES (:role_name)", USERROLES_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("role_name", userRole.getRoleName().name());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        userRole.setId(id);
        return userRole;
    }

    @Override
    public void update(UserRoles userRole,Integer id) {
        String query = String.format("UPDATE %s SET role_name=? WHERE id= ?", USERROLES_TABLE_NAME);
        jdbcTemplate.update(query,userRole.getRoleName().name(),id);
    }

    @Override
    public List<UserRoles> addAll(List<UserRoles> userRoles) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (role_name) VALUES (:role_name)", USERROLES_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            UserRoles userRole = userRoles.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("role_name", userRole.getRoleName().name());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return userRoles;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", USERROLES_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
