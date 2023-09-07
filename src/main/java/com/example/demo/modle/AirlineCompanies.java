package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class AirlineCompanies {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected String name;
    @Getter @Setter
    protected Integer countryId;
    @Getter @Setter
    protected Integer userId;

    public AirlineCompanies(Integer id, String name, Integer countryId, Integer userId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "AirlineCompanies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryId=" + countryId +
                ", userId=" + userId +
                '}';
    }
}
