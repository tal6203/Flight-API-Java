package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineCompanyDetails {
    protected Integer id;
    protected String name;
    protected String country;
    protected Integer user_id;


    public AirlineCompanyDetails(Integer id, String name, String country, Integer user_id) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AirlineCompanyDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
