package com.example.demo.controllers;

import com.example.demo.Services.UserRolesService;
import com.example.demo.modle.UserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/userRoles")
public class UserRolesController {

    @Autowired
    UserRolesService userRolesService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserRoles> getUserRoleById(@PathVariable Integer id) {
        try {
            UserRoles userRole = userRolesService.getById(id);
            if (userRole != null) {
                return new ResponseEntity<>(userRole, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRoles>> getAllUserRoles() {
        try {
            List<UserRoles> userRoles = userRolesService.getAll();
            return new ResponseEntity<>(userRoles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<UserRoles> addUserRole(@RequestBody UserRoles userRole) {
        try {
            UserRoles resultUserRole = userRolesService.add(userRole);
            return new ResponseEntity<>(resultUserRole, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoles> updateUserRole(@PathVariable Integer id, @RequestBody UserRoles userRole) {
        try {
            UserRoles resultUserRole = userRolesService.getById(id);
            userRolesService.update(userRole,id);
            if (resultUserRole != null) {
                return new ResponseEntity<>(resultUserRole, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserRoles> deleteUserRole(@PathVariable Integer id) {
        try {
            UserRoles resultUserRole = userRolesService.getById(id);
            if (resultUserRole != null) {
                userRolesService.remove(id);
                return new ResponseEntity<>(resultUserRole, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
