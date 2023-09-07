package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithAirlineCompanyDTO {
    @Getter@Setter
    private Users user;

    @Getter@Setter
    private AirlineCompanyDetails airlineCompany;

    public UserWithAirlineCompanyDTO(Users user, AirlineCompanyDetails airlineCompany) {
        user.setUserRole(Roles.AirlineCompany.getValue());
        this.user = user;
        this.airlineCompany = airlineCompany;
    }

    @Override
    public String toString() {
        return "UserWithAirlineCompanyDTO{" +
                "user=" + user.toString() +
                ", airlineCompany=" + airlineCompany.toString() +
                '}';
    }
}
