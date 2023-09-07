package com.example.demo.Repository;

import com.example.demo.modle.Users;

import java.util.List;

public interface IUsersRepository  {
    Users getById(Integer id);

    List<Users> getAll();

    Users add(Users user);

    void update(Users user,Integer id);

    List<Users> addAll(List<Users> users);

    void remove(Integer id);

}
