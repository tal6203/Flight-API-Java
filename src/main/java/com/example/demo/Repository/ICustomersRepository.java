package com.example.demo.Repository;
import com.example.demo.modle.Customers;
import java.util.List;

public interface ICustomersRepository {
    Customers getById(Integer id);

    List<Customers> getAll();

    Customers add(Customers customer);

    void update(Customers customer,Integer id);

    List<Customers> addAll(List<Customers> customers);

    void remove(Integer id);
}
