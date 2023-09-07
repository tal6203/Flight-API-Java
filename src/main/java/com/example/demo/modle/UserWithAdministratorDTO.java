package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithAdministratorDTO {
    @Getter@Setter
    private Users user;
    @Getter@Setter
    private Administrators administrator;

    public UserWithAdministratorDTO(Users user, Administrators administrator) {
        user.setUserRole(Roles.Administrator.getValue());
        this.user = user;
        this.administrator = administrator;
    }

    @Override
    public String toString() {
        return "UserWithAdministratorDTO{" +
                "user=" + user.toString() +
                ", administrator=" + administrator.toString() +
                '}';
    }
}
