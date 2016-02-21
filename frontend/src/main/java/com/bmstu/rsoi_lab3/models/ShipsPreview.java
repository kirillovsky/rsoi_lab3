package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.ShipBackend;

/**
 * Created by Александр on 09.02.2016.
 */

public class ShipsPreview implements ShipBackend{

    private Long id;
    private String name;
    private String country;

    public ShipsPreview(Ships s) {
        this.id = s.getId();
        this.name = s.getName();
        this.country = s.getCountry();
    }

    public ShipsPreview(Long id, String name, String country) {
        this.name = name;
        this.id = id;
        this.country = country;
    }

    public ShipsPreview() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
