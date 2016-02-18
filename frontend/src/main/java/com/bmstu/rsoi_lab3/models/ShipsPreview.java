package com.bmstu.rsoi_lab3.models;

/**
 * Created by Александр on 09.02.2016.
 */

public class ShipsPreview {

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

}
