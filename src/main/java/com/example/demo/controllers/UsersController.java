package com.example.demo.controllers;

import com.example.demo.Services.UsersService;
import com.example.demo.modle.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UsersService usersService;
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR') or hasRole('AIRLINE_COMPANY')")
    @GetMapping("/ById/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        try {
            Users user = usersService.getById(id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR') or hasRole('AIRLINE_COMPANY')")
    @GetMapping("/ByEmail/{email}")
    public ResponseEntity<Users>  getUserByEmail(@PathVariable String email) {
        try {
            Users user = usersService.getUserByEmail(email);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR') or hasRole('AIRLINE_COMPANY')")
    @GetMapping("/ByUsername/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        try {
            Users user = usersService.getByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> users = usersService.getAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        try {
            Users resultUser = usersService.add(user);
            return new ResponseEntity<>(resultUser, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMINISTRATOR') or hasRole('AIRLINE_COMPANY')")
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users user) {
        try {
            Users resultUser = usersService.getById(id);
            if (resultUser != null) {
                usersService.update(user,id);
                return new ResponseEntity<>(resultUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable Integer id) {
        try {
            Users resultUser = usersService.getById(id);
            if (resultUser != null) {
                usersService.remove(id);
                return new ResponseEntity<>(resultUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
