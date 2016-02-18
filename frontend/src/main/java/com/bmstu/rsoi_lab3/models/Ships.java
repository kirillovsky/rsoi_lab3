package com.bmstu.rsoi_lab3.models;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Александр on 08.02.2016.
 */

public class Ships implements Serializable {

    private Long id;
    private String name;
    private String type;
    private String country;

    public Ships(String name, String type, String country) {
        this.name = name;
        this.type = type;
        this.country = country;
    }

    public Ships() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Ships{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", country='" + country  +
                '}';
    }
}
