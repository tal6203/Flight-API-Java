package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class UserRoles {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected Roles roleName;

    public UserRoles(Integer id, Roles roleName) {
        this.id = id;
        this.roleName = roleName;
    }




    @Override
    public String toString() {
        return "UserRoles{" +
                "id=" + id +
                ", roleName='" + roleName.name() + '\'' +
                '}';
    }
}
