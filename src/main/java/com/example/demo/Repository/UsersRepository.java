package com.example.demo.Repository;

import com.example.demo.modle.Users;
import com.example.demo.modle.UsersMapper;
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
public class UsersRepository implements IUsersRepository{
    protected static final String USERS_TABLE_NAME = "users";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    public Users getUserByUsername(String username) {
        String query = String.format("SELECT * FROM %s WHERE username = ?",USERS_TABLE_NAME);
        try {
            return jdbcTemplate.queryForObject(query, new UsersMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsByUsername(String username) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE username = ?",USERS_TABLE_NAME);
        int count = jdbcTemplate.queryForObject(query, Integer.class, username);
        return count == 0;
    }

    public boolean existsByEmail(String email) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE email = ?",USERS_TABLE_NAME);
        int count = jdbcTemplate.queryForObject(query, Integer.class, email);
        return count == 0;
    }


    public Users getUserByEmail(String email) {
        try {
            String query = String.format("SELECT * FROM %s WHERE email=?",USERS_TABLE_NAME);
            return jdbcTemplate.queryForObject(query, new UsersMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            // Handle any other exceptions
            throw new RuntimeException("Failed to get user by email: " + e.getMessage());
        }
    }


    @Override
    public Users getById(Integer id) {
        String query = String.format("Select * from %s where id=?", USERS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new UsersMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Users> getAll() {
        String query = String.format("Select * from %s", USERS_TABLE_NAME);
        return jdbcTemplate.query(query, new UsersMapper());
    }

    @Override
    public Users add(Users user) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (username, password, email, user_role) " +
                "VALUES (:username, :password, :email, :user_role)", USERS_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("email", user.getEmail());
        params.addValue("user_role", user.getUserRole());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        user.setId(id);
        return user;
    }

    @Override
    public void update(Users user,Integer id) {
        String query = String.format("UPDATE %s SET username=?, password=?, email=?, user_role=? WHERE id= ?", USERS_TABLE_NAME);
        jdbcTemplate.update(query, user.getUsername(), user.getPassword(), user.getEmail(),user.getUserRole(),id);
    }


    public void updatePassword(String password,Integer id) {
        String query = String.format("UPDATE %s SET password=? WHERE id= ?", USERS_TABLE_NAME);
        jdbcTemplate.update(query, password ,id);
    }

    @Override
    public List<Users> addAll(List<Users> users) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (username, password, email, user_role) " +
                "VALUES (:username, :password, :email, :user_role)", USERS_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[users.size()];
        for (int i = 0; i < users.size(); i++) {
            Users user = users.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("username", user.getUsername());
            params.addValue("password", user.getPassword());
            params.addValue("email", user.getEmail());
            params.addValue("user_role", user.getUserRole());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return users;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", USERS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }

    public Users getByUsername(String username) {
        String query = String.format("Select * from %s where username=?", USERS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new UsersMapper(), username);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
