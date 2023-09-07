package com.example.demo.Repository;

import com.example.demo.modle.Customers;
import com.example.demo.modle.CustomersMapper;
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
public class CustomersRepository implements ICustomersRepository{
    protected static final String CUSTOMERS_TABLE_NAME = "customers";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    public Customers getCustomerByUsername(String username) {
        String query = "SELECT * FROM get_customer_by_username(?)";
        try {
            return jdbcTemplate.queryForObject(query, new CustomersMapper(), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public boolean existsByPhone(String phone) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE phone_no = ?",CUSTOMERS_TABLE_NAME);
        int count = jdbcTemplate.queryForObject(query, Integer.class, phone);
        return count == 0;
    }

    public boolean existsByCreditCard(String CreditCard) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE credit_card_no = ?",CUSTOMERS_TABLE_NAME);
        int count = jdbcTemplate.queryForObject(query, Integer.class, CreditCard);
        return count == 0;
    }

    @Override
    public Customers getById(Integer id) {
        String query = String.format("Select * from %s where id=?", CUSTOMERS_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new CustomersMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Customers> getAll() {
        String query = String.format("Select * from %s", CUSTOMERS_TABLE_NAME);
        return jdbcTemplate.query(query, new CustomersMapper());
    }

    @Override
    public Customers add(Customers customer) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (first_name, last_name, address, phone_no, credit_card_no, user_id) " +
                "VALUES (:first_name, :last_name, :address, :phone_no, :credit_card_no, :user_id)", CUSTOMERS_TABLE_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("first_name", customer.getFirst_name());
        params.addValue("last_name", customer.getLast_name());
        params.addValue("address", customer.getAddress());
        params.addValue("phone_no", customer.getPhone_no());
        params.addValue("credit_card_no", customer.getCredit_card_no());
        params.addValue("user_id", customer.getUser_id());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(queryNamedParam, params, generatedKeyHolder);
        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
        customer.setId(id);
        return customer;
    }

    @Override
    public void update(Customers customer,Integer id) {
        String query = String.format("UPDATE %s SET first_name=?, last_name=?, address=?, phone_no=?, credit_card_no=?, user_id=? WHERE id=?", CUSTOMERS_TABLE_NAME);
        jdbcTemplate.update(query, customer.getFirst_name(), customer.getLast_name(), customer.getAddress(),customer.getPhone_no(),
                customer.getCredit_card_no(),customer.getUser_id(),id);

    }

    @Override
    public List<Customers> addAll(List<Customers> customers) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        String queryNamedParam = String.format("INSERT INTO %s (first_name, last_name, address, phone_no, credit_card_no, user_id) " +
                "VALUES (:first_name, :last_name, :address, :phone_no, :credit_card_no, :user_id)", CUSTOMERS_TABLE_NAME);
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            Customers customer = customers.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("first_name", customer.getFirst_name());
            params.addValue("last_name", customer.getLast_name());
            params.addValue("address", customer.getAddress());
            params.addValue("phone_no", customer.getPhone_no());
            params.addValue("credit_card_no", customer.getCredit_card_no());
            params.addValue("user_id", customer.getUser_id());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(queryNamedParam, paramsArray);

        return customers;
    }

    @Override
    public void remove(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", CUSTOMERS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }
}
