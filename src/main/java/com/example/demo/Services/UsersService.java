package com.example.demo.Services;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.modle.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersService implements IUsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Value("${min_PasswordLength}")
    private Integer min_PasswordLength;

    public Users getUserByEmail(String email) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String checkUsername = usersRepository.getUserByEmail(email).getUsername();
        if (currentUsername.equals(checkUsername)) {
            return usersRepository.getUserByEmail(email);
        } else {
            throw new RuntimeException("Only a user can edit herself");
        }
    }

    public Users getByUsername(String username){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String checkUsername = username;
        if (currentUsername.equals(checkUsername)) {
            return usersRepository.getByUsername(username);
        } else {
            throw new RuntimeException("Only a user can edit herself");
        }
    }

    @Override
    public Users getById(Integer id) {
        return usersRepository.getById(id);
    }

    @Override
    public List<Users> getAll() {
        return usersRepository.getAll();
    }

    @Override
    public Users add(Users user) {
        validateUserDetails(user);
        return usersRepository.add(user);
    }

    @Override
    public void update(Users user, Integer id) {
        validateUserDetails(user);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = user.getUsername();
        if (currentUsername.equals(username)) {
            usersRepository.update(user, id);
        } else {
            throw new RuntimeException("Only a user can edit herself");
        }
    }

    @Override
    public List<Users> addAll(List<Users> users) {
        for (Users user : users) {
            validateUserDetails(user);
        }
        return usersRepository.addAll(users);
    }

    @Override
    public void remove(Integer id) {

        usersRepository.remove(id);
    }

    private void validateUserDetails(Users user) {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("One or more of the details are incorrect");
        }

        if (user.getPassword().length() <= min_PasswordLength) {
            throw new IllegalArgumentException("Password length should be greater than " + min_PasswordLength);
        }
    }
}
