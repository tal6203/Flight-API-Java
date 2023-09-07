package com.example.demo.modle;

import lombok.Getter;

public class JwtResponse {
    @Getter
    private Users user;
    @Getter
    private String token;


    public JwtResponse(Users user, String token) {
        this.user = user;
        this.token = token;
    }


    @Override
    public String toString() {
        return "JwtResponse{" +
                "user=" + user.toString() +
                ", token='" + token + '\'' +
                '}';
    }
}


