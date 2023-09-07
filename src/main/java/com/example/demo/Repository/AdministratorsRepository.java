package com.example.demo.Repository;

import com.example.demo.modle.Administrators;
import com.example.demo.modle.AdministratorsMapper;
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
public class AdministratorsRepository implements IAdministratorsRepository{
    protected static final String ADMINISTRATORS_TABLE_NAME = "administrators";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    @Override
    public List<Administrators> getAll() {
        String query = String.format("Select * from %s", ADMINISTRATORS_TABLE_NAME);
        return jdbcTemplate.query(query, new AdministratorsMapper());
    }

    @Override
    public Administrators getById(Integer id) {
        String query = String.format("Select * from %s where id=?", ADMINISTRATORS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new AdministratorsMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Administrators add(Administrators administrator) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (first_name, last_name, user_id) " +
                "VALUES (:first_name, :last_name, :user_id)", ADMINISTRATORS_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("first_name", administrator.getFirstName());
        params.addValue("last_name", administrator.getLastName());
        params.addValue("user_id", administrator.getUser_id());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        administrator.setId(id);
        return administrator;
    }

    @Override
    public void update(Administrators administrator,Integer id) {
        String query = String.format("UPDATE %s SET first_name=?, last_name=?, user_id=? WHERE id= ?", ADMINISTRATORS_TABLE_NAME);
        jdbcTemplate.update(query, administrator.getFirstName(), administrator.getLastName(), administrator.getUser_id(),id);
    }

    @Override
    public List<Administrators> addAll(List<Administrators> administrators) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (first_name, last_name, user_id) " +
                "VALUES (:first_name, :last_name, :user_id)", ADMINISTRATORS_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[administrators.size()];
        for (int i = 0; i < administrators.size(); i++) {
            Administrators administrator = administrators.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("first_name", administrator.getFirstName());
            params.addValue("last_name", administrator.getLastName());
            params.addValue("user_id", administrator.getUser_id());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return administrators;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", ADMINISTRATORS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
