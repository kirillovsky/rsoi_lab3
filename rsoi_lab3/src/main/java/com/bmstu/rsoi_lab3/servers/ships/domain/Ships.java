package com.bmstu.rsoi_lab3.servers.ships.domain;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Александр on 08.02.2016.
 */

@Entity
public class Ships implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
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
