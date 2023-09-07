package com.example.demo.modle;

import lombok.Getter;
import lombok.Setter;

public class Countries {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected String name;

    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        return "countries{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
