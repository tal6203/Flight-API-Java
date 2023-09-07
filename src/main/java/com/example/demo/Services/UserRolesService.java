package com.example.demo.Services;

import com.example.demo.Repository.CacheRepository;
import com.example.demo.Repository.UserRolesRepository;
import com.example.demo.modle.UserRoles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Service
public class UserRolesService implements IUserRolesService{

    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private CacheRepository cacheRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_ROLES_CACHE_KEY = "UserRoles_cache";
    private static final int CACHE_EXPIRATION_TIME = 300; // 5 minutes

    @Value("${cache_on}")
    private Boolean cache_on;

    @Override
    public UserRoles getById(Integer id) {
        return userRolesRepository.getById(id);
    }

    public List<UserRoles> getAll() {
        if (cache_on && cacheRepository.isKeyExist(USER_ROLES_CACHE_KEY)) {
            String userRolesList = cacheRepository.getCacheEntity(USER_ROLES_CACHE_KEY);
            if (!userRolesList.isEmpty()) {
                try {
                    List<UserRoles> userRoles = Arrays.asList(objectMapper.readValue(userRolesList, UserRoles[].class));
                    System.out.println("Brings the user roles from the cache");
                    return userRoles;
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        List<UserRoles> userRolesList = userRolesRepository.getAll();
        if (cache_on && !userRolesList.isEmpty()) {
            try {
                String userRolesListString = objectMapper.writeValueAsString(userRolesList);
                cacheRepository.createCacheEntity(USER_ROLES_CACHE_KEY, userRolesListString);
                cacheRepository.setKeyExpiration(USER_ROLES_CACHE_KEY, CACHE_EXPIRATION_TIME);
            } catch (JsonProcessingException e) {
                System.out.println(e);
            }
        }

        return userRolesList;
    }

    @Override
    public UserRoles add(UserRoles userRole) {
           return userRolesRepository.add(userRole);
    }

    @Override
    public void update(UserRoles userRole, Integer id) {
            userRolesRepository.update(userRole,id);
    }

    @Override
    public List<UserRoles> addAll(List<UserRoles> userRoles) {
        return  userRolesRepository.addAll(userRoles);
    }

    @Override
    public void remove(Integer id) {
            userRolesRepository.remove(id);
    }
}
