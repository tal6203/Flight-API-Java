package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class Users {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected String username;
    @Getter @Setter

    protected String password;
    @Getter @Setter

    protected String email;
    @Getter @Setter

    protected Integer userRole;

    public Users(Integer id, String username, String password, String email, Integer userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userRoleId=" + userRole +
                '}';
    }
}
