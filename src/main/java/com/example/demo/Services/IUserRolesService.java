package com.example.demo.Services;

import com.example.demo.modle.UserRoles;

import java.util.List;

public interface IUserRolesService {
    UserRoles getById(Integer id);

    List<UserRoles> getAll();

    UserRoles add(UserRoles userRole);

    void update(UserRoles userRole,Integer id);

    List<UserRoles> addAll(List<UserRoles> userRoles);

    void remove(Integer id);
}
