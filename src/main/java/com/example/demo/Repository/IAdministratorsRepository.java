package com.example.demo.Repository;
import com.example.demo.modle.Administrators;

import java.util.List;
public interface IAdministratorsRepository {
    List<Administrators> getAll();

    Administrators getById(Integer id);
    Administrators add(Administrators administrator);

    void update(Administrators administrator,Integer id);

    List<Administrators> addAll(List<Administrators> administrators);

    void remove(Integer id);
}
