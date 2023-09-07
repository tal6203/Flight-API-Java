package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class Administrators {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected String firstName;
    @Getter @Setter
    protected String lastName;
    @Getter @Setter
    protected Integer user_id;

    public Administrators(Integer id, String firstName, String lastName, Integer user_id) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_id = user_id;
    }


    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId=" + user_id +
                '}';
    }
}
