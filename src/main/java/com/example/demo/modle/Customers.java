package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class Customers {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected String first_name;
    @Getter @Setter
    protected String last_name;
    @Getter @Setter
    protected String address;
    @Getter @Setter
    protected String phone_no;
    @Getter @Setter
    protected String credit_card_no;
    @Getter @Setter
    protected Integer user_id;

    public Customers(Integer id, String firstName, String lastName, String address, String phoneNo, String creditCardNo, Integer userId) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        this.address = address;
        this.phone_no = phoneNo;
        this.credit_card_no = creditCardNo;
        this.user_id = userId;
    }


    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", credit_card_no='" + credit_card_no + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
