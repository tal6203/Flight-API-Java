package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithCustomerDTO {
    @Getter
    @Setter
    private Users user;
    @Getter
    @Setter
    private Customers customer;

    public UserWithCustomerDTO(Users user, Customers customer) {
        user.setUserRole(Roles.Customer.getValue());
        this.user = user;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "UserWithCustomerDTO{" +
                "user=" + user.toString() +
                ", customer=" + customer.toString() +
                '}';
    }
}
