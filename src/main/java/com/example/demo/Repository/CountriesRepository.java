package com.example.demo.Repository;

import com.example.demo.modle.Countries;
import com.example.demo.modle.CountriesMapper;
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
public class CountriesRepository implements ICountriesRepository{
    protected static final String COUNTRIES_TABLE_NAME = "countries";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    @Override
    public Countries getById(Integer id) {
        String query = String.format("Select * from %s where id=?", COUNTRIES_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new CountriesMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Countries> getAll() {
        String query = String.format("Select * from %s", COUNTRIES_TABLE_NAME);
        return jdbcTemplate.query(query, new CountriesMapper());
    }

    @Override
    public Countries add(Countries country) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (name) VALUES (:name)", COUNTRIES_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", country.getName());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        country.setId(id);
        return country;
    }

    @Override
    public void update(Countries country,Integer id) {
        String query = String.format("UPDATE %s SET name=? WHERE id= ?", COUNTRIES_TABLE_NAME);
        jdbcTemplate.update(query, country.getName(),id);
    }

    @Override
    public List<Countries> addAll(List<Countries> countries) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (name) VALUES (:name)", COUNTRIES_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[countries.size()];
        for (int i = 0; i < countries.size(); i++) {
            Countries country = countries.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", country.getName());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);
        return countries;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", COUNTRIES_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
